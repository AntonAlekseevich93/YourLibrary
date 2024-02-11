package authors_screen

import ApplicationTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthorFirstLetterItem(
    letter: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = letter,
        modifier = modifier.padding(bottom = 2.dp, start = 16.dp),
        style = ApplicationTheme.typography.title2Bold,
        color = ApplicationTheme.colors.mainTextColor,
    )
}