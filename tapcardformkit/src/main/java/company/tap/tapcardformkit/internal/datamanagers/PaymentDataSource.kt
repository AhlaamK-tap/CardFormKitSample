package company.tap.tapcardformkit.internal.datamanagers

import company.tap.tapcardformkit.internal.api.responses.SDKSettings
import company.tap.tapcardformkit.internal.interfaces.PaymentDataSource
import company.tap.tapcardformkit.open.SdkMode

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
object PaymentDataSource : PaymentDataSource{

    private var sdkMode: SdkMode = SdkMode.SAND_BOX
    private var sdkSettings: SDKSettings? = null

    /**
     * Set sdkSettings.
     *
     * @param sdkSettings the sdkSettings
     */
    fun setSDKSettings(sdkSettings: SDKSettings?) {
        this.sdkSettings = sdkSettings
    }

    /**
     * Set sdkMode.
     *
     * @param sdkMode the sdkMode
     */
    fun setSDKMode(sdkMode: SdkMode){
        this.sdkMode = sdkMode
    }
    override fun getSDKSettings(): SDKSettings? {
        return sdkSettings
    }

    override fun getSDKMode(): SdkMode {
        return  sdkMode
    }
}