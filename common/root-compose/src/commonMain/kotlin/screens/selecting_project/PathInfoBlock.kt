package screens.selecting_project

import ApplicationTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import models.PathInfoVo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import text_fields.CommonTextField
import java.awt.Desktop
import java.io.File


@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun PathInfoBlock(
    pathInfo: PathInfoVo,
    selectedPathInfo: () -> Unit,
    renamePath: (newName: String) -> Unit,
    restartApp: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val interactionIconSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val isHoveredIcon = interactionIconSource.collectIsHoveredAsState()
    var showMenu by remember { mutableStateOf(false) }
    var renameMode by remember { mutableStateOf(false) }
    var needRestartApp by remember { mutableStateOf(false) }
    val textField: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue(text = pathInfo.libraryName)) }
    val cardBackground = if (isHovered.value && !renameMode && !needRestartApp) {
        ApplicationTheme.colors.pointerIsActiveCardColor
    } else {
        Color.Transparent
    }

    val iconBackground = if (isHoveredIcon.value) {
        ApplicationTheme.colors.mainIconsColor
    } else {
        ApplicationTheme.colors.searchIconColor
    }

    androidx.compose.material3.Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 22.dp)
            .fillMaxWidth()
            .clickable(interactionSource, null) {
                if (!needRestartApp) {
                    selectedPathInfo.invoke()
                }
            }
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(cardBackground)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (renameMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        CommonTextField(
                            modifier = Modifier.sizeIn(maxWidth = 140.dp, minWidth = 140.dp),
                            textState = textField.value,
                            onTextChanged = {
                                textField.value = it
                            },
                        )

                        Text(
                            text = Strings.save,
                            style = ApplicationTheme.typography.footnoteRegular,
                            color = ApplicationTheme.colors.mainTextColorLight,
                            modifier = Modifier.padding(start = 8.dp).clickable {
                                renameMode = false
                                if (pathInfo.libraryName != textField.value.text && textField.value.text.length > 1) {
                                    renamePath.invoke(textField.value.text.trim())
                                    needRestartApp = true
                                }
                            }
                        )

                        Text(
                            text = Strings.cancel,
                            style = ApplicationTheme.typography.footnoteRegular,
                            color = ApplicationTheme.colors.mainTextColorLight,
                            modifier = Modifier.padding(start = 8.dp).clickable {
                                renameMode = false
                                textField.value = textField.value.copy(text = pathInfo.libraryName)
                            }
                        )
                    }
                } else {
                    Text(
                        text = pathInfo.libraryName,
                        style = ApplicationTheme.typography.bodyBold,
                        color = ApplicationTheme.colors.mainTextColorLight,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Column {
                    if (!renameMode) {
                        Image(
                            painter = painterResource(Drawable.drawable_ic_menu_button),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(iconBackground),
                            modifier = Modifier.size(20.dp).padding(start = 8.dp)
                                .clickable(interactionSource, null) {
                                    showMenu = true
                                }
                                .hoverable(
                                    interactionSource = interactionIconSource,
                                    enabled = true,
                                )
                        )
                    }

                    Card(shape = RoundedCornerShape(4.dp)) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 12.dp
                                )
                            ) {
                                Text(
                                    text = Strings.rename_library,
                                    style = ApplicationTheme.typography.footnoteRegular,
                                    color = ApplicationTheme.colors.mainTextColorLight,
                                    modifier = Modifier.padding(bottom = 8.dp).clickable {
                                        renameMode = true
                                        showMenu = false
                                    }
                                )

                                Text(
                                    text = "Показать в Finder", //todo нужно определять OS
                                    style = ApplicationTheme.typography.footnoteRegular,
                                    color = ApplicationTheme.colors.mainTextColorLight,
                                    modifier = Modifier.padding(bottom = 0.dp).clickable {
                                        try {
                                            Desktop.getDesktop().open(File(pathInfo.path))
                                            showMenu = false
                                        } catch (_: Throwable) {
                                            //todo log
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = pathInfo.path,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.textDescriptionColor,
            )

            AnimatedVisibility(needRestartApp) {
                Button(
                    onClick = restartApp,
                    modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApplicationTheme.colors.primaryButtonColor),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    Text(
                        text = Strings.reboot,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColorLight
                    )
                }
            }
        }
    }
}