package loader

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
fun LoadingProcessWithTitle(text: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = ApplicationTheme.colors.hintColor, strokeWidth = 2.dp)
        Text(
            text = text,
            style = ApplicationTheme.typography.footnoteRegularItalic,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}