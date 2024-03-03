package join_authors_screen.components

import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.AuthorVo
import models.AuthorsEvents
import tags.CustomTag

@Composable
fun BaseEventScope<BaseEvent>.JoinInSearchItemClickMenu(
    originalAuthor: State<AuthorVo>,
    modifiedAuthor: AuthorVo,
    onClickAsMain: () -> Unit,
) {
    Row(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 12.dp)) {
        CustomTag(
            text = Strings.add_to_relates,
            onClick = {
                this@JoinInSearchItemClickMenu.sendEvent(
                    AuthorsEvents.AddAuthorToRelates(
                        originalAuthor = originalAuthor.value,
                        modifiedAuthorId = modifiedAuthor.id,
                    )
                )
            },
            modifier = Modifier.padding(end = 8.dp)
        )
        CustomTag(
            text = Strings.as_main,
            onClick = onClickAsMain
        )
    }

}