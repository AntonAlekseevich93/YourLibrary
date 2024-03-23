package models

import BaseEvent

sealed class UserEvents : BaseEvent {

    data object OnSignUpClick: UserEvents()
    data object OnSignInClick: UserEvents()
    data object OnSignInConfirmClick: UserEvents()
    data object OnSignUpConfirmClick: UserEvents()
}