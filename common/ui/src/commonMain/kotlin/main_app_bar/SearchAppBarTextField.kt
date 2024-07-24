package main_app_bar

import ApplicationTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import text_fields.CommonTextField

@Composable
fun SearchAppBarTextField(
    modifier: Modifier = Modifier,
    hintText: String = "",
    iconResName: DrawableResource? = null,
    onTextChanged: ((text: String) -> Unit),
) {
    val interactionSource = remember { MutableInteractionSource() }
    var textState by remember { mutableStateOf(TextFieldValue()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.Transparent),
        border = BorderStroke(
            1.dp,
            color = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconResName?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp).size(18.dp)
                )
            }

            CommonTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 46.dp)
                    .weight(1f),
                textState = textState,
                onTextChanged = {
                    textState = it
                    onTextChanged(it.text)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = ApplicationTheme.colors.mainTextColor,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = hintText,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.hintColor
                    )
                },
                maxLines = 1,
                textStyle = ApplicationTheme.typography.bodyRegular,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp).size(18.dp)
            )
        }
    }
}