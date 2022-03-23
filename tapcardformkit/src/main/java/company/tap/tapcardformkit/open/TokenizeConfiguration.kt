package company.tap.tapcardformkit.open

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import company.tap.tapcardformkit.internal.api.ApiService
import company.tap.tapcardformkit.internal.api.CardViewEvent
import company.tap.tapcardformkit.internal.api.CardViewModel
import company.tap.tapcardformkit.internal.api.models.TapCardDataConfiguration
import company.tap.tapcardformkit.internal.datamanagers.PaymentDataSource
import company.tap.tapnetworkkit.connection.NetworkApp

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@SuppressLint("StaticFieldLeak")
object TokenizeConfiguration  {

    @JvmField
    var tokenizeDelegate: TokenizeDelegate? = null
    private var defaultCardHolderName: String? = null
    lateinit var _context: Context


    /**
     * Set default CardholderName.
     *
     * @param defaultCardHolderName the  default CardholderName
     */
    fun setDefaultCardHolderName(defaultCardHolderName: String?) {
        this.defaultCardHolderName = defaultCardHolderName
    }

    /**
    Handles tokenizing the card forum engine with the required data to be able to tokenize on demand. It calls the Tokenize api
    - Parameter tapCardForm: The tapcardform required to handle UI updates
     */

    @RequiresApi(Build.VERSION_CODES.N)
    fun startTokenize(tapCardForm: TapCardForm) {
            startTokenizingCard(tapCardForm)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startTokenizingCard(tapCardForm: TapCardForm) {
        CardViewModel().processEvent(
            CardViewEvent.CreateTokenEvent,
            tapCardForm.getCard(),
            _context ,
            tapCardForm
            )
    }


    fun addCardTokenizeDelegate(_tokenizeDelegate: TokenizeDelegate) {
        println("_tokenizeDelegate sdk ${_tokenizeDelegate}")
        this.tokenizeDelegate = _tokenizeDelegate


    }


    fun getListener(): TokenizeDelegate? {
        return tokenizeDelegate
    }


    /**
    Handles initializing the card forum engine with the required data to be able to tokenize on demand. It calls the Init api
    - Parameter dataConfig: The data configured by you as a merchant (e.g. secret key, locale, etc.)
     */

    @RequiresApi(Build.VERSION_CODES.N)
    fun initCardForm(context: Context, dataConfig: TapCardDataConfiguration) {
        initializeCardForm(context,dataConfig)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initializeCardForm(context: Context, dataConfig: TapCardDataConfiguration) {
        NetworkApp.initNetwork(
            context,
            dataConfig.authToken,
            context.packageName,
            ApiService.BASE_URL
        )
        CardViewModel().processEvent(
            CardViewEvent.InitEvent,
            null, context, null
        )
        _context = context
    }

    /*   private fun isInternetConnectionAvailable(): ErrorTypes? {
           val ctx: Context = SDKSession.contextSDK
               ?: return ErrorTypes.SDK_NOT_CONFIGURED_WITH_VALID_CONTEXT
           val connectivityManager =
               ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
           if (connectivityManager != null) {
               val activeNetworkInfo = connectivityManager.activeNetworkInfo
               return if (activeNetworkInfo != null && activeNetworkInfo.isConnected) ErrorTypes.INTERNET_AVAILABLE else ErrorTypes.INTERNET_NOT_AVAILABLE
           }
           return ErrorTypes.CONNECTIVITY_MANAGER_ERROR
       }*/
}
