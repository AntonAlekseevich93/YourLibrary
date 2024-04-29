package book_editor.elements.book_selector.elements

import ApplicationTheme
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingBookProcess() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = ApplicationTheme.colors.hintColor, strokeWidth = 2.dp)
        Text(
            text = Strings.loading_book_search_info,
            style = ApplicationTheme.typography.footnoteRegularItalic,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}