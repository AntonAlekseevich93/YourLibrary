package models

import BaseEvent

sealed class UserEvents : BaseEvent {

    data object OnSignUpClick : UserEvents()
    data object OnSignInClick : UserEvents()
    data class OnSignInConfirmClick(val email: String, val password: String) : UserEvents()
    data class OnSignUpConfirmClick(val name: String, val email: String, val password: String) :
        UserEvents()

    data object OnSignOut : UserEvents()
    data object GetUserIfVerified : UserEvents()
}