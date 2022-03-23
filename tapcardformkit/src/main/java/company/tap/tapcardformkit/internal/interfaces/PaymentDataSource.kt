package company.tap.tapcardformkit.internal.interfaces

import company.tap.tapcardformkit.internal.api.responses.SDKSettings
import company.tap.tapcardformkit.open.SdkMode

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
interface PaymentDataSource {
    fun getSDKSettings(): SDKSettings?

    /**
     * Defines the SDK mode . Optional. @return the default Sandbox
     */
    fun getSDKMode(): SdkMode?
}