package authors_screen

import ApplicationTheme
import AuthorsViewModel
import Strings
import alert_dialog.CommonAlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import di.Inject
import models.AuthorsEvents
import renaming_author.RenamingAuthorBlock
import toolbar.CommonToolbar

@Composable
fun AuthorsScreen(
    showLeftDrawer: State<Boolean>,
) {
    val viewModel = remember { Inject.instance<AuthorsViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val renamingAuthorTextField: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(uiState.authorAlertDialogState.value.show) {
        renamingAuthorTextField.value =
            renamingAuthorTextField.value.copy(text = uiState.authorAlertDialogState.value.selectedAuthor.name)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = uiState.snackbarHostState.value)
        },
        content = {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ApplicationTheme.colors.mainBackgroundColor)
                ) {
                    viewModel.CommonToolbar(showLeftDrawer) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = Strings.autors,
                            style = ApplicationTheme.typography.title3Bold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )

                    }
                    viewModel.AuthorsScreenContent(uiState)
                }

                AnimatedVisibility(uiState.authorAlertDialogState.value.show) {
                    uiState.authorAlertDialogState.value.apply {
                        CommonAlertDialog(
                            config = config,
                            acceptListener = {
                                viewModel.sendEvent(
                                    AuthorsEvents.RenameAuthor(
                                        authorId = selectedAuthor.id,
                                        newName = renamingAuthorTextField.value.text
                                    )
                                )
                                viewModel.sendEvent(AuthorsEvents.HideAlertDialog)
                            },
                            onDismissRequest = {
                                viewModel.sendEvent(AuthorsEvents.HideAlertDialog)
                            },
                            content = {
                                RenamingAuthorBlock(
                                    textField = renamingAuthorTextField,
                                    onTextChanged = {
                                        renamingAuthorTextField.value = it
                                    }
                                )
                            }
                        )
                    }
                }
            }
        })
}