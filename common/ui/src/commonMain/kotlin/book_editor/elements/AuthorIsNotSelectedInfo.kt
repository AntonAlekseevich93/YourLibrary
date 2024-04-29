package book_editor.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import info.InfoBlock

@Composable
fun AuthorIsNotSelectedInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoBlock(
            text = Strings.error_author_can_exist,
            textColor = ApplicationTheme.colors.errorColor,
        )
    }
}