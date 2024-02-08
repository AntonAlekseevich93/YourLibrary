import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthorFirstLetterItem(
    letter: String
) {
    Text(
        text = letter,
        modifier = Modifier.padding(bottom = 16.dp),
        style = ApplicationTheme.typography.title1Bold,
        color = ApplicationTheme.colors.mainTextColor,
    )
}