package company.tap.tapcardformkit.internal.api

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonElement
import company.tap.tapcardformkit.internal.api.models.CreateTokenCard
import company.tap.tapcardformkit.internal.api.models.Token
import company.tap.tapcardformkit.internal.api.requests.CreateTokenWithCardDataRequest
import company.tap.tapcardformkit.internal.api.responses.SDKSettings
import company.tap.tapcardformkit.internal.datamanagers.PaymentDataSource
import company.tap.tapcardformkit.open.TapCardForm
import company.tap.tapcardformkit.open.TokenizeConfiguration
import company.tap.tapnetworkkit.controller.NetworkController
import company.tap.tapnetworkkit.enums.TapMethodType
import company.tap.tapnetworkkit.exception.GoSellError
import company.tap.tapnetworkkit.interfaces.APIRequestCallback
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
class CardRepository : APIRequestCallback {
    val resultObservable = BehaviorSubject.create<CardViewState>()
    lateinit var tokenResponse: Token

    lateinit var cardRepositoryContext: Context
    lateinit var _tapCardForm: TapCardForm
    private lateinit var cardViewModel: CardViewModel

    private var tokenizeParams: TokenizeConfiguration = TokenizeConfiguration
    private var initResponse: SDKSettings? = null

    @RequiresApi(Build.VERSION_CODES.N)
    fun getInitData(_context: Context, cardViewModel: CardViewModel?) {
        if (cardViewModel != null) {
            this.cardViewModel = cardViewModel
        }
        NetworkController.getInstance().processRequest(
            TapMethodType.GET,
            ApiService.INIT,
            null,
            this,
            INIT_CODE
        )
        this.cardRepositoryContext = _context
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun createTokenWithEncryptedCard(

        createTokenWithCardDataRequest: CreateTokenCard?,
        tapCardForm: TapCardForm?
    ) {

        val createTokenWithCardDataReq = createTokenWithCardDataRequest?.let {
            CreateTokenWithCardDataRequest(
                it
            )
        }
        val jsonString = Gson().toJson(createTokenWithCardDataReq)
        NetworkController.getInstance().processRequest(
            TapMethodType.POST, ApiService.TOKEN, jsonString,
            this, CREATE_TOKEN_CODE
        )
        if (tapCardForm != null) {
            this._tapCardForm = tapCardForm
        }
    }

    override fun onSuccess(responseCode: Int, requestCode: Int, response: Response<JsonElement>?) {
        if (requestCode == INIT_CODE) {
            response?.body().let {
                initResponse = Gson().fromJson(it, SDKSettings::class.java)
                PaymentDataSource.setSDKSettings(initResponse)
            }
        } else if (requestCode == CREATE_TOKEN_CODE) {
            response?.body().let {
                tokenResponse = Gson().fromJson(it, Token::class.java)
                if (tokenResponse != null) {
                    tokenizeParams.getListener()?.cardTokenizedSuccessfully(tokenResponse)
                    _tapCardForm.clearCardInputAction()

                }
            }

        }
    }

    override fun onFailure(requestCode: Int, errorDetails: GoSellError?) {
        errorDetails?.let {
            if (it.throwable != null) {
                resultObservable.onError(it.throwable)
                tokenizeParams.getListener()?.backendUnknownError("Required fields are empty")

            } else
                try {
                    // resultObservable.onError(Throwable(it.errorMessage))
                    RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)

                    tokenizeParams.getListener()?.cardTokenizedFailed(errorDetails)

                } catch (e: Exception) {

                }


        }
    }

    companion object {
        private const val INIT_CODE = 1
        private const val CREATE_TOKEN_CODE = 2

    }

}