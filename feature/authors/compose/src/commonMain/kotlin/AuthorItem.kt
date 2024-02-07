import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.AuthorVo

@Composable
fun AuthorItem(
    author: AuthorVo
) {
    Text(
        text = author.name,
        modifier = Modifier.padding(bottom = 16.dp),
        style = ApplicationTheme.typography.footnoteRegular,
        color = ApplicationTheme.colors.mainTextColor,
    )
}