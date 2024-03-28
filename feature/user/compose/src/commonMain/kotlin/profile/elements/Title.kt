package profile.elements

import ApplicationTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Title(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = ApplicationTheme.typography.title2Bold,
        color = ApplicationTheme.colors.textDescriptionColor,
        modifier = modifier,
    )
}