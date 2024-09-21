package auth.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import text_fields.OldCommonTextField

@Composable
fun SignUpFields(
    nameTextField: MutableState<TextFieldValue>,
    emailTextField: MutableState<TextFieldValue>,
    passwordTextField: MutableState<TextFieldValue>,
) {
    val hiddenPasswordTransformation = VisualTransformation { text ->
        TransformedText(
            text = AnnotatedString.Builder().apply {
                repeat(text.length) {
                    append('*')
                }
            }.toAnnotatedString(),
            offsetMapping = OffsetMapping.Identity
        )
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OldCommonTextField(
            modifier = Modifier.sizeIn(maxWidth = 340.dp).padding(bottom = 12.dp).fillMaxWidth(),
            textState = nameTextField.value,
            onTextChanged = {
                nameTextField.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ApplicationTheme.colors.cardBackgroundDark,
                textColor = ApplicationTheme.colors.mainTextColor,
                disabledTextColor = ApplicationTheme.colors.mainTextColor
            ),
            placeholder = {
                Text(
                    text = Strings.user_name,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            }
        )

        OldCommonTextField(
            modifier = Modifier.sizeIn(maxWidth = 340.dp).padding(bottom = 12.dp).fillMaxWidth(),
            textState = emailTextField.value,
            onTextChanged = {
                emailTextField.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ApplicationTheme.colors.cardBackgroundDark,
                textColor = ApplicationTheme.colors.mainTextColor,
                disabledTextColor = ApplicationTheme.colors.mainTextColor
            ),
            placeholder = {
                Text(
                    text = Strings.email,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            }
        )

        OldCommonTextField(
            modifier = Modifier.sizeIn(maxWidth = 340.dp).padding(bottom = 12.dp).fillMaxWidth(),
            textState = passwordTextField.value,
            onTextChanged = {
                passwordTextField.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = ApplicationTheme.colors.cardBackgroundDark,
                textColor = ApplicationTheme.colors.mainTextColor,
                disabledTextColor = ApplicationTheme.colors.mainTextColor
            ),
            placeholder = {
                Text(
                    text = Strings.password,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            },
            visualTransformation = hiddenPasswordTransformation
        )
    }
}