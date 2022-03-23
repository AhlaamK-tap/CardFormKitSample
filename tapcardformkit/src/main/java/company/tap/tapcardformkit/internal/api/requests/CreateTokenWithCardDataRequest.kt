package company.tap.tapcardformkit.internal.api.requests

import androidx.annotation.RestrictTo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import company.tap.tapcardformkit.internal.api.models.CreateTokenCard
import company.tap.tapcardformkit.internal.interfaces.CreateTokenRequest

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@RestrictTo(RestrictTo.Scope.LIBRARY)
class CreateTokenWithCardDataRequest(card: CreateTokenCard) : CreateTokenRequest {
    @SerializedName("card")
    @Expose
    val card: CreateTokenCard = card

}