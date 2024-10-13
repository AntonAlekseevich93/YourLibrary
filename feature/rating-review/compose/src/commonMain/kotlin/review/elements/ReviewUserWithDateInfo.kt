package review.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
internal fun ReviewUserWithDateInfo(
    userName: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = userName,
            style = ApplicationTheme.typography.footnoteBold,
            color = ApplicationTheme.colors.mainTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = date,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.hintColor
        )
    }
}