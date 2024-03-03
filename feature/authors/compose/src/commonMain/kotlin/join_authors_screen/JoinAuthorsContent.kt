package join_authors_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable.drawable_ic_arrow_in_box
import Drawable.drawable_ic_join
import alert_dialog.CommonAlertDialog
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import join_authors_screen.components.JoinAllAuthors
import join_authors_screen.components.JoinMainAuthorBlock
import join_authors_screen.components.JoinRelatesAuthorBlock
import main_models.AuthorVo
import models.AuthorsEvents
import models.JoiningAuthorsUiState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import renaming_author.RenamingAuthorBlock

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.JoinAuthorsContent(
    state: State<JoiningAuthorsUiState>,
    searchingAuthorsResult: State<LinkedHashMap<String, MutableList<AuthorVo>>>
) {
    var showCommonAlertDialog by remember { mutableStateOf(false) }
    var alertDialogConfig: CommonAlertDialogConfig? by remember { mutableStateOf(null) }
    var newAuthorAsMain by remember { mutableStateOf(AuthorVo.getEmptyAuthor()) }
    val renamingAuthorTextField: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue()) }

    Box {
        Column(modifier = Modifier.padding(top = 24.dp)) {
            JoinMainAuthorBlock(state.value.mainAuthor)

            CenterBoxContainer {
                Image(
                    painter = painterResource(drawable_ic_join),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    modifier = Modifier
                        .rotate(90f)
                        .padding(vertical = 8.dp)
                        .size(36.dp)
                )
            }

            JoinRelatesAuthorBlock(
                mainAuthor = state.value.mainAuthor,
                showAlertDialog = { config, newAuthor ->
                    alertDialogConfig = config
                    newAuthorAsMain = newAuthor
                    if (config.showContent) {
                        renamingAuthorTextField.value =
                            renamingAuthorTextField.value.copy(text = newAuthor.name)
                    }
                    showCommonAlertDialog = true
                }
            )

            CenterBoxContainer {
                Image(
                    painter = painterResource(drawable_ic_arrow_in_box),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(34.dp)
                )
            }

            JoinAllAuthors(
                originalAuthor = state.value.mainAuthor,
                authorsByAlphabet = state.value.allAuthorsExceptMainAndRelates,
                searchingResult = searchingAuthorsResult,
                showAlertDialog = { config, newAuthor ->
                    alertDialogConfig = config
                    newAuthorAsMain = newAuthor
                    showCommonAlertDialog = true
                }
            )
        }

        AnimatedVisibility(showCommonAlertDialog && alertDialogConfig != null) {
            CommonAlertDialog(
                config = alertDialogConfig!!,
                acceptListener = {
                    if (alertDialogConfig?.showContent == true) {
                        this@JoinAuthorsContent.sendEvent(
                            AuthorsEvents.RenameAuthor(
                                authorId = newAuthorAsMain.id,
                                newName = renamingAuthorTextField.value.text
                            )
                        )
                    } else {
                        this@JoinAuthorsContent.sendEvent(
                            AuthorsEvents.SetAuthorAsMain(
                                oldAuthorId = state.value.mainAuthor.value.id,
                                newAuthorId = newAuthorAsMain.id,
                                newAuthorName = newAuthorAsMain.name
                            )
                        )
                    }
                    newAuthorAsMain = AuthorVo.getEmptyAuthor()
                    showCommonAlertDialog = false
                },
                onDismissRequest = {
                    newAuthorAsMain = AuthorVo.getEmptyAuthor()
                    showCommonAlertDialog = false
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