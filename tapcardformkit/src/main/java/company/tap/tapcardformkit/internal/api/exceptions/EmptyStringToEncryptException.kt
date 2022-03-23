package company.tap.tapcardformkit.internal.api.exceptions

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
object EmptyStringToEncryptException : IllegalArgumentException() {
    val MESSAGE = "Parameter jsonString cannot be empty"

    /**
     * Instantiates a new Empty string to encrypt exception.
     */
    fun EmptyStringToEncryptException() {
        (MESSAGE)
    }
}