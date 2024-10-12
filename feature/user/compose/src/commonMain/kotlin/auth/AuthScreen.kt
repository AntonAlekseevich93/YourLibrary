package auth

import ApplicationTheme
import ProfileAppBar
import UserViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import auth.elements.ButtonSwitcher
import auth.elements.SignInFields
import auth.elements.SignUpFields
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import di.Inject
import models.UserEvents
import navigation.screen_components.ProfileScreenComponent
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.app_logo

@Composable
fun AuthScreen(
    isHazeBlurEnabled: Boolean,
    hazeState: HazeState,
    navigationComponent: ProfileScreenComponent
) {
    val viewModel = remember { Inject.instance<UserViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val hazeModifier: Modifier = if (isHazeBlurEnabled) {
        Modifier.haze(
            state = hazeState,
            style = HazeStyle(
                tint = Color.Black.copy(alpha = .04f),
                blurRadius = 30.dp,
            )
        )
    } else Modifier
    val userName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val userEmail: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val userPassword: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    val buttonIsActive = remember { mutableStateOf(false) }

    if (uiState.isSignUnState.value) {
        buttonIsActive.value =
            userName.value.text.length >= 2 && userEmail.value.text.contains("@") && userPassword.value.text.length >= 6
    } else {
        buttonIsActive.value =
            userEmail.value.text.contains("@") && userPassword.value.text.length >= 6
    }
    Scaffold(
        topBar = {
            ProfileAppBar(
                hazeBlurState = hazeState,
                isHazeBlurEnabled = isHazeBlurEnabled,
                title = "Авторизация",
                showBackButton = false,
                onSettings = {
                    navigationComponent.onSettingsClick()
                },
                onBack = {
                }
            )
        },
        containerColor = ApplicationTheme.colors.cardBackgroundDark,
    ) {
        Column(
            modifier = Modifier
                .padding(top = 1.dp) //fixes haze blur bug
                .verticalScroll(scrollState)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = hazeModifier.fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            ) {
                Image(
                    painter = painterResource(Res.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 44.dp)
                )

                if (uiState.isSignUnState.value) {
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

                viewModel.ButtonSwitcher(
                    isSignUnState = uiState.isSignUnState,
                    buttonIsActive = buttonIsActive,
                    onClick = {
                        val event = if (uiState.isSignUnState.value) {
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
                        viewModel.sendEvent(event)
                    }
                )
            }
        }
    }
}