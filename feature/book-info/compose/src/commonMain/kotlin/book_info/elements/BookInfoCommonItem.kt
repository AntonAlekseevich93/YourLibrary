package book_info.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun BookInfoCommonItem(
    title: String,
    description: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = ApplicationTheme.typography.headlineBold.copy(fontSize = 18.sp),
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = description,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            softWrap = true,
            letterSpacing = TextUnit.Unspecified,
        )
    }
}