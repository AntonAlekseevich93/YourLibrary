package authors_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import main_models.AuthorVo

@Composable
fun BaseEventScope<BaseEvent>.AuthorItem(
    author: AuthorVo,
    showAuthorMenuEvent: (id: String) -> Unit,
    hideAuthorMenu: State<String>
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val cardBackground = if (isHovered.value) {
        ApplicationTheme.colors.pointerIsActiveCardColor
    } else {
        Color.Transparent
    }
    var showAuthorMenu by remember { mutableStateOf(false) }

    if (hideAuthorMenu.value != author.id && showAuthorMenu) {
        showAuthorMenu = false
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource, null) {
                showAuthorMenu = !showAuthorMenu
                showAuthorMenuEvent.invoke(author.id)
            }
            .hoverable(
                interactionSource = interactionSource,
                enabled = true,
            ),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(cardBackground)
    ) {
        Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)) {
            Text(
                text = author.name,
                style = ApplicationTheme.typography.bodyRegular,
                color = ApplicationTheme.colors.mainTextColor,
            )

            if (author.relatedAuthors.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = author.relatedAuthors.joinToString { it.name },
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            }
        }
    }

    AnimatedVisibility(
        visible = showAuthorMenu,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally(targetOffsetX = { -500 })
    ) {
        AuthorItemMenu(author)
    }
}