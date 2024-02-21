package join_authors_screen.components

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.AuthorVo
import models.AuthorsEvents

@Composable
fun BaseEventScope<BaseEvent>.RelatesAuthorsDropMenu(
    modifiedAuthor: AuthorVo,
    originalAuthor: AuthorVo,
    onClose: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = Strings.remove_from_relates,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp).clickable {
                this@RelatesAuthorsDropMenu.sendEvent(
                    AuthorsEvents.RemoveAuthorFromRelates(
                        originalAuthor = originalAuthor,
                        modifiedAuthorId = modifiedAuthor.id
                    )
                )
                onClose.invoke()
            }
        )
        Text(
            text = Strings.as_main_author,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = Strings.rename,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = Strings.delete,
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding()
        )
    }
}