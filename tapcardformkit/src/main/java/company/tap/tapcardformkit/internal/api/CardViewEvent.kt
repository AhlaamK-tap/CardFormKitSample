package company.tap.tapcardformkit.internal.api

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
sealed class CardViewEvent {
    object InitEvent : CardViewEvent()
    object CreateTokenEvent :CardViewEvent()
}