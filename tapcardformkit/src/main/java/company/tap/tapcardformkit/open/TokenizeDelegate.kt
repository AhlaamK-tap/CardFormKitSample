package company.tap.tapcardformkit.open

import company.tap.tapcardformkit.internal.api.models.Token
import company.tap.tapnetworkkit.exception.GoSellError

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
interface TokenizeDelegate {
    fun cardTokenizedSuccessfully(token: Token)
    fun cardTokenizedFailed(goSellError: GoSellError?)
    fun backendUnknownError(message: String?)
}