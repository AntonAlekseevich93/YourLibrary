package screens.selecting_project

import ApplicationTheme
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import text_fields.CommonTextField


@Composable
internal fun CreateLibraryBlock(
    path: String,
    showDirPicker: () -> Unit,
    createVaultListener: (name: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val textField: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    var showErrorMessage by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 44.dp)
    ) {
        AnimatedVisibility(visible = showErrorMessage) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = Strings.creating_vault_error,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.errorColor,
            )
        }

        Row {
            Spacer(Modifier.weight(1f, fill = true))
            Column(modifier = Modifier.weight(4f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = "⬅ ${Strings.back}",
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.textDescriptionColor,
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onBackClick.invoke() }.padding(start = 10.dp)
                )

                Text(
                    text = Strings.create_library,
                    style = ApplicationTheme.typography.headlineRegular,
                    color = ApplicationTheme.colors.mainTextColorLight,
                    modifier = Modifier.padding(start = 10.dp, top = 16.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp),
                    thickness = 1.dp,
                    color = ApplicationTheme.colors.divider
                )
            }

            Spacer(Modifier.weight(1f, fill = true))
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f, fill = true))
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = Strings.library_name,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColorLight
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = Strings.library_name_description,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.textDescriptionColor
                )
            }
            Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.CenterEnd) {
                Card(
                    border = BorderStroke(1.dp, ApplicationTheme.colors.divider),
                    backgroundColor = ApplicationTheme.colors.textFieldColorDark,
                    modifier = Modifier
                ) {
                    CommonTextField(
                        modifier = Modifier.sizeIn(maxWidth = 140.dp, minWidth = 140.dp),
                        textState = textField.value,
                        onTextChanged = {
                            if (showErrorMessage) {
                                showErrorMessage = false
                            }
                            textField.value = it
                        },
                    )
                }
            }
            Spacer(Modifier.weight(1f, fill = true))
        }

        Row {
            Spacer(Modifier.weight(1f, fill = true))
            Box(modifier = Modifier.weight(4f), contentAlignment = Alignment.CenterStart) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp),
                    thickness = 1.dp,
                    color = ApplicationTheme.colors.divider
                )
            }
            Spacer(Modifier.weight(1f, fill = true))
        }

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f, fill = true))
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = Strings.location,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColorLight
                )

                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = Strings.location_description,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.textDescriptionColor,
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = {
                        showErrorMessage = false
                        showDirPicker.invoke()
                    },
                    modifier = Modifier.sizeIn(maxHeight = 30.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApplicationTheme.colors.secondaryButtonColor),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    Text(
                        text = Strings.select,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColorLight
                    )
                }
            }
            Spacer(Modifier.weight(1f, fill = true))
        }

        AnimatedVisibility(visible = path.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = path,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.primaryButtonColor,
            )
        }

        Button(
            onClick = {
                if (path.isNotEmpty() && textField.value.text.length >= 2) {
                    val name = textField.value.text.trim()
                    showErrorMessage = false
                    createVaultListener.invoke(name)
                } else {
                    showErrorMessage = true
                }
            },
            modifier = Modifier.padding(top = 28.dp).sizeIn(maxHeight = 30.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ApplicationTheme.colors.primaryButtonColor),
            contentPadding = PaddingValues(horizontal = 10.dp),
        ) {
            Text(
                text = Strings.create,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.mainTextColorLight
            )
        }
    }
}
