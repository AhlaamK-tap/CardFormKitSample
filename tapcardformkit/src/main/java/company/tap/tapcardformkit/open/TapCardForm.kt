package company.tap.tapcardformkit.open

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import cards.pay.paycardsrecognizer.sdk.FrameManager
import cards.pay.paycardsrecognizer.sdk.ui.InlineViewFragment
import company.tap.cardinputwidget.widget.inline.InlineCardInput
import company.tap.nfcreader.open.reader.TapEmvCard
import company.tap.nfcreader.open.reader.TapNfcCardReader
import company.tap.nfcreader.open.utils.TapNfcUtils
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.internal.api.models.CreateTokenCard
import company.tap.tapcardvalidator_android.CardValidator
import company.tap.taplocalizationkit.LocalizationManager
import company.tap.tapuilibrary.themekit.ThemeManager
import company.tap.tapuilibrary.uikit.interfaces.TapPaymentShowHideClearImage
import company.tap.tapuilibrary.uikit.views.TapSelectionTabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables


/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
class TapCardForm(context: Context?, attrs: AttributeSet?) :
LinearLayout(context, attrs), TapPaymentShowHideClearImage
    {


    var  paymentInputContainer: LinearLayout
    var tapCardInputView: InlineCardInput
    private var cardNumber: String? = null
    private var expiryDate: String? = null
    private var cvvNumber: String? = null
    var  clearView: ImageView
    private var cardScannerBtn: ImageView? = null
    private var nfcButton: ImageView? = null
    var tabLayout: TapSelectionTabLayout? = null
    private lateinit var cardSchema: String
    private var shouldShowScannerOptions = true
    private  var tapNfcCardReader: TapNfcCardReader
    private var cardReadDisposable: Disposable = Disposables.empty()
        private val inlineViewFragment = InlineViewFragment()
        lateinit var inLineCardLayout: FrameLayout

    init {
        ThemeManager.loadTapTheme(resources, R.raw.defaultlighttheme, "lighttheme")
        LocalizationManager.loadTapLocale(resources, R.raw.lang)
        LinearLayout.inflate(context, R.layout.activity_tap_card_form, this)

        paymentInputContainer = findViewById(R.id.payment_input_layout)
        inLineCardLayout   = findViewById(R.id.inline_container)
        tapCardInputView = context?.let { InlineCardInput(it, null) }!!
        clearView = findViewById(R.id.clear_text)
        tapNfcCardReader = TapNfcCardReader(context as Activity?)

        initCardForm()
        initializeCardForm()
        initClearText()

    }

    private fun initClearText() {
        clearView.setOnClickListener {
            clearCardInputAction()
        }
    }

    fun clearCardInputAction() {
        tapCardInputView.clear()

    }

    private fun initializeCardForm() {
        tabLayout = findViewById(R.id.sections_tablayout)
        tabLayout?.visibility = View.GONE
        cardScannerBtn = findViewById(R.id.card_scanner_button)
        nfcButton = findViewById(R.id.nfc_button)
        cardScannerBtn?.visibility = View.VISIBLE
        nfcButton?.visibility = View.VISIBLE

        clearView.visibility = View.GONE
        nfcButton?.setOnClickListener {
           // onClickNFC()
        }
        cardScannerBtn?.setOnClickListener {
          // onClickCardScanner()
            inLineCardLayout.visibility = View.VISIBLE
           /* FrameManager.getInstance().frameColor = Color.WHITE
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.inline_container, InlineViewFragment())
                .commit()*/
        }
    }

    private fun initCardForm() {
        tapCardInputView.holderNameEnabled = false
        paymentInputContainer.addView(tapCardInputView)
        tapCardInputView.clearFocus()
        cardNumberWatcher()
        expiryDateWatcher()
        cvcNumberWatcher()
    }

    private fun cardNumberWatcher() {
        tapCardInputView.setCardNumberTextWatcher(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onCardTextChange(s)
                cardNumAfterTextChangeListener(s, this)
            }
        })
    }

    private fun onCardTextChange(s: CharSequence?) {
        if (s.toString().isEmpty()) {
            clearView.visibility = View.GONE
            // tapAlertView?.visibility = View.GONE
        } else {
            clearView.visibility = View.VISIBLE
        }
    }

    private fun expiryDateWatcher() {
        tapCardInputView.setExpiryDateTextWatcher(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChangeAction(s)
            }
        })
    }

    private fun afterTextChangeAction(s: Editable?) {
        if (s.isNullOrEmpty()) {

        } else {
            /**
             * we will get date value
             */
            expiryDate = s.toString()
            if (s.length >= 5) {
                /*   tapAlertView?.alertMessage?.text = (LocalizationManager.getValue(
                       "Warning",
                       "Hints",
                       "missingCVV"
                   ))*/
                // tapAlertView?.visibility = View.VISIBLE

            }


        }
    }


    private fun cvcNumberWatcher() {
        tapCardInputView.setCvcNumberTextWatcher(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                /**
                 * we will get cvv number
                 */
                cvvNumber = s.toString()
            }
        })
    }

    fun cardNumAfterTextChangeListener(charSequence: CharSequence?, textWatcher: TextWatcher) {
        val card = CardValidator.validate(charSequence.toString())

        /*   if (charSequence != null) {
               baseLayoutManager.resetViewHolder()

               if (charSequence.length > 2) callCardBinNumberApi(charSequence, textWatcher) else {
                   tabLayout.resetBehaviour()
                   PaymentDataSource.setBinLookupResponse(null)
               }
           }
           charSequence?.let {
               if (charSequence.isNullOrEmpty()) {
                   tapAlertView?.visibility = View.GONE
               }
               if (card?.cardBrand != null) {
                   tabLayout.selectTab(
                       card.cardBrand,
                       card.validationState == CardValidationState.valid
                   )

                   println("card brand value is>>>"+card.cardBrand)

                   val binLookupResponse: BINLookupResponse? =
                       PaymentDataSource.getBinLookupResponse()
                   // println("binLookupResponse" + binLookupResponse)
                   if(charSequence.length>4) checkIfCardTypeExistInList(card.cardBrand)
                   if (PaymentDataSource.getCardType() != null && PaymentDataSource.getCardType() == CardType.ALL) {

                       setTabLayoutBasedOnApiResponse(binLookupResponse, card)
                   } else {
                       checkAllowedCardTypes(binLookupResponse)
                       setTabLayoutBasedOnApiResponse(binLookupResponse, card)
                   }
               }

               *//**
         * we will get the full card number
         */
        cardNumber = charSequence.toString()
        //lastCardInput = it.toString()
        // shouldShowScannerOptions = it.isEmpty()
        controlScannerOptions()
        cardBrandDetection(charSequence.toString())
        // if (card != null)  checkValidationState(card)
    }


    override fun showHideClearImage(show: Boolean) {
        if (show) {
            clearView.visibility = View.VISIBLE
        } else {
            clearView.visibility = View.GONE

        }
    }



    // Logic to show the switches when card details are valid
    private fun cardBrandDetection(cardTyped: String) {

        val card = CardValidator.validate(cardTyped)
        // checkValidationState(card.cardBrand)
        // if (card.cardBrand != null && ::cardSchema.isInitialized) {
        if (card.cardBrand != null ) {
            println("card brand: ${card.validationState}")
            nfcButton?.visibility = View.GONE
            cardScannerBtn?.visibility = View.GONE
        }
    }

    private fun controlScannerOptions() {
        if (shouldShowScannerOptions) {
            if (TapNfcUtils.isNfcAvailable(context)) {
                nfcButton?.visibility = View.VISIBLE
            } else nfcButton?.visibility = View.GONE

            cardScannerBtn?.visibility = View.VISIBLE
        } else {

            nfcButton?.visibility = View.GONE
            cardScannerBtn?.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun handleNFCResult(intent: Intent?) {
        if (tapNfcCardReader.isSuitableIntent(intent)) {
            cardReadDisposable = tapNfcCardReader
                .readCardRx2(intent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ emvCard: TapEmvCard? ->
                    if (emvCard != null) {
                        /*              callBinLookupApi(emvCard.cardNumber?.substring(0, 6))

                                      Handler().postDelayed({
                                          val binLookupResponse: BINLookupResponse? = PaymentDataSource.getBinLookupResponse()
                                          if (PaymentDataSource.getCardType() != null && PaymentDataSource.getCardType() == CardType.ALL) {
                                              setNfcCardDetails(emvCard)

                                          } else {
                                              if (binLookupResponse != null) {
                                                  paymentInputViewHolder.checkAllowedCardTypes(binLookupResponse)
                                                  setNfcCardDetails(emvCard)
                                              }

                                          }
                                      }, 300)*/
                        println("emvCard$emvCard")
                    }
                },
                    { throwable -> throwable.message?.let { println("error is nfc" + throwable.printStackTrace()) } })
        }

    }

    fun getCard(): CreateTokenCard? {
        val number: String? = cardNumber
        val expiryDate: String? = expiryDate
        val cvc: String? = cvvNumber
        //temporrary    val cardholderName: String? = cardholderName
    /*    var cardholderName: String? =null
        if(PaymentDataSource.getDefaultCardHolderName()!=null) {
            cardholderName = PaymentDataSource.getDefaultCardHolderName()
        }else {
            cardholderName = "cardholderName"
        }*/
         val cardholderName: String = "cardholder"
        return if (number == null || expiryDate == null || cvc == null) {
            null
        } else {
            val dateParts: List<String> = expiryDate.split("/")
            return dateParts.get(0).let {
                CreateTokenCard(
                    number.replace(" ", ""),
                    it,
                    dateParts[1],
                    cvc,
                    cardholderName, null
                )
            }
        }
        // TODO: Add address handling here.
    }

}

