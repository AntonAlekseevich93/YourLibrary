package error

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_empty_search

@Composable
internal fun SearchError(
    title: String?,
    titleAnnotationString: AnnotatedString?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_empty_search),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
            modifier = Modifier
                .padding(top = 16.dp)
                .size(128.dp)
        )
        title?.let {
            Text(
                text = it,
                style = ApplicationTheme.typography.title3Bold,
                color = ApplicationTheme.colors.mainTextColor,
                textAlign = TextAlign.Center
            )
        }
        titleAnnotationString?.let {
            Text(
                text = it,
                style = ApplicationTheme.typography.title3Bold,
                color = ApplicationTheme.colors.mainTextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}