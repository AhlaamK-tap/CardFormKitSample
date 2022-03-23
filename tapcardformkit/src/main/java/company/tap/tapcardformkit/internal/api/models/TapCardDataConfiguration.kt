package company.tap.tapcardformkit.internal.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import company.tap.tapcardformkit.internal.interfaces.BaseResponse
import company.tap.tapcardformkit.open.SdkMode
import java.io.Serializable

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
data class TapCardDataConfiguration(
    @SerializedName("authToken") @Expose
    var authToken: String? = null,

    @SerializedName("language")
    @Expose
    val language: String? = null,
    @SerializedName("sdkMode")
    @Expose
    val sdkMode: SdkMode? = SdkMode.SAND_BOX,
):Serializable,BaseResponse
