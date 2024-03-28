package auth

import BaseEvent
import BaseEventScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import auth.elements.ButtonSwitcher
import auth.elements.SignInFields
import auth.elements.SignUpFields
import models.UserEvents
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.AuthScreen(isSignUnState: State<Boolean>) {
    val userName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val userEmail: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val userPassword: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val buttonIsActive = remember { mutableStateOf(false) }

    if (isSignUnState.value) {
        buttonIsActive.value =
            userName.value.text.length >= 2 && userEmail.value.text.contains("@") && userPassword.value.text.length >= 6
    } else {
        buttonIsActive.value =
            userEmail.value.text.contains("@") && userPassword.value.text.length >= 6
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Drawable.drawable_app_logo),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 44.dp)
        )

        if (isSignUnState.value) {
            SignUpFields(
                nameTextField = userName,
                emailTextField = userEmail,
                passwordTextField = userPassword
            )
        } else {
            SignInFields(
                emailTextField = userEmail,
                passwordTextField = userPassword
            )
        }

        ButtonSwitcher(
            isSignUnState = isSignUnState,
            buttonIsActive = buttonIsActive,
            onClick = {
                val event = if (isSignUnState.value) {
                    UserEvents.OnSignUpConfirmClick(
                        name = userName.value.text,
                        email = userEmail.value.text,
                        password = userPassword.value.text
                    )
                } else {
                    UserEvents.OnSignInConfirmClick(
                        email = userEmail.value.text,
                        password = userPassword.value.text
                    )
                }
                this@AuthScreen.sendEvent(event)
            }
        )
    }
}