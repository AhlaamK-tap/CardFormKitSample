package company.tap.tapcardformkit.internal.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import company.tap.tapcardformkit.internal.api.models.CreateTokenCard
import company.tap.tapcardformkit.open.TapCardForm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
class CardViewModel : ViewModel() {

    private val repository = CardRepository()
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context
    private val liveData = MutableLiveData<Resource<CardViewState>>()


    init {
        compositeDisposable.add(repository.resultObservable
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { liveData.value = Resource.Loading() }
            .doOnTerminate { liveData.value = Resource.Finished() }
            .subscribe(
                { data -> liveData.value = Resource.Success(data) },
                { error -> liveData.value = error.message?.let { Resource.Error(it) } }
            ))

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getInitData(cardViewModel: CardViewModel?,_context: Context?) {
        if (_context != null) {
            this.context = _context
            repository.getInitData(_context, cardViewModel)
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun processEvent(
        event: CardViewEvent,
        cardDataRequest: CreateTokenCard?,
        context: Context? = null,
        tapCardForm: TapCardForm?
    ) {
        when (event) {
            CardViewEvent.InitEvent -> getInitData( this ,context)
            CardViewEvent.CreateTokenEvent -> createTokenWithEncryptedCard(cardDataRequest,
                    tapCardForm )

        }
    }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun createTokenWithEncryptedCard( createTokenWithEncryptedDataRequest: CreateTokenCard?, tapCardForm:TapCardForm?) {
            //  println("createTokenWithEncryptedDataRequest>>."+createTokenWithEncryptedDataRequest)
          //  if (createTokenWithEncryptedDataRequest != null) {
                repository.createTokenWithEncryptedCard(createTokenWithEncryptedDataRequest, tapCardForm)
         //   }

        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}