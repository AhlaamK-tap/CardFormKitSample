package company.tap.cardformkit

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import company.tap.tapcardformkit.internal.api.models.TapCardDataConfiguration
import company.tap.tapcardformkit.internal.api.models.Token
import company.tap.tapcardformkit.open.SdkMode
import company.tap.tapcardformkit.open.TapCardForm
import company.tap.tapcardformkit.open.TokenizeDelegate
import company.tap.tapcardformkit.open.TokenizeConfiguration
import company.tap.tapnetworkkit.exception.GoSellError

class MainActivity : AppCompatActivity() , TokenizeDelegate {
    var tokenizeConfiguration: TokenizeConfiguration = TokenizeConfiguration
    lateinit var tapCardForm :TapCardForm
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCardForm()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initCardForm() {
        tapCardForm = findViewById(R.id.tapCardForm)
        tokenizeConfiguration.addCardTokenizeDelegate(this) //** Required **
        tokenizeConfiguration.initCardForm(this, TapCardDataConfiguration("sk_test_kovrMB0mupFJXfNZWx6Etg5y","en",SdkMode.SAND_BOX))

    }


    override fun cardTokenizedSuccessfully(token: Token) {
        println("Card Tokenized Succeeded : ")
        println("Token card : " + token.card?.firstSix.toString() + " **** " + token.card?.lastFour)
        println("Token card : " + token.card?.fingerprint.toString() + " **** " + token.card?.funding)
        println("Token card : " + token.card?.id.toString() + " ****** " + token.card?.name)
        println("Token card : " + token.card?.address.toString() + " ****** " + token.card?.`object`)
        println("Token card : " + token.card?.expirationMonth.toString() + " ****** " + token.card?.expirationYear)
        Toast.makeText(this, "cardTokenizedSuccessfully" + token.id, Toast.LENGTH_SHORT).show()
    }

    override fun cardTokenizedFailed(goSellError: GoSellError?) {
        println("sdkError> errorMessage>>>>" + goSellError?.errorMessage)
        println("sdkError errorBody>>>>>" + goSellError?.errorBody)
        Toast.makeText(this, "TOkenizationfailed" +goSellError?.errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun backendUnknownError(message: String?) {
        println("backendUnknownError> errorMessage>>>>" + message)
        //println("backendUnknownError errorBody>>>>>" + message?.throwable)
        Toast.makeText(this, "backendUnknownError" +message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun tokenizeCard(view: View) {
        tokenizeConfiguration.startTokenize(tapCardForm)
    }
}