package navigation_drawer.contents

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import main_models.BookItemVo
import main_models.BooksInfoHeader
import main_models.TooltipPosition
import tooltip_area.TooltipEvents
import tooltip_area.TooltipIconArea

@Composable
fun BaseEventScope<BaseEvent>.LeftDrawerBooksContent(
    booksInfoUiState: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookItemVo>>,
    openBookListener: (bookId: String) -> Unit,
) {
    var booksButtonIsSelected by remember { mutableStateOf(true) }
    var authorButtonIsSelected by remember { mutableStateOf(false) }
    var bookShelvesButtonIsSelected by remember { mutableStateOf(false) }
    val booksLazyState = rememberLazyListState()

    fun disableSelection() {
        if (booksButtonIsSelected) booksButtonIsSelected = false
        if (authorButtonIsSelected) authorButtonIsSelected = false
        if (bookShelvesButtonIsSelected) bookShelvesButtonIsSelected = false
    }

    Column {
        Row(
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TooltipIconArea(
                text = Strings.all_books,
                iconTint = if (booksButtonIsSelected) ApplicationTheme.colors.selectedIconColor
                else ApplicationTheme.colors.mainIconsColor,
                drawableResName = Drawable.drawable_ic_book,
                tooltipCallback = {
                    this@LeftDrawerBooksContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.BOTTOM
                    }))
                },
                modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                onClick = {
                    disableSelection()
                    booksButtonIsSelected = true
                },
                disableSelection = booksButtonIsSelected
            )

            TooltipIconArea(
                text = Strings.authors,
                iconTint = if (authorButtonIsSelected) ApplicationTheme.colors.selectedIconColor
                else ApplicationTheme.colors.mainIconsColor,
                drawableResName = Drawable.drawable_ic_authors,
                tooltipCallback = {
                    this@LeftDrawerBooksContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.BOTTOM
                    }))
                },
                modifier = Modifier.padding(end = 6.dp),
                disableSelection = authorButtonIsSelected,
                onClick = {
                    disableSelection()
                    authorButtonIsSelected = true
                }
            )

            TooltipIconArea(
                text = Strings.shelves,
                iconTint = if (bookShelvesButtonIsSelected) ApplicationTheme.colors.selectedIconColor
                else ApplicationTheme.colors.mainIconsColor,
                drawableResName = Drawable.drawable_ic_book_shelves,
                tooltipCallback = {
                    this@LeftDrawerBooksContent.sendEvent(TooltipEvents.SetTooltipEvent(it.apply {
                        position = TooltipPosition.BOTTOM
                    }))
                },
                modifier = Modifier.padding(end = 6.dp),
                disableSelection = bookShelvesButtonIsSelected,
                onClick = {
                    disableSelection()
                    bookShelvesButtonIsSelected = true
                }
            )
        }

        AnimatedVisibility(
            visible = booksButtonIsSelected,
            enter = slideInHorizontally(
                initialOffsetX = { -300 },
                animationSpec = tween(durationMillis = 100),
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 100),
                targetOffsetX = { -300 }
            )
        ) {
            DrawerBooksInfoContent(
                booksLazyState = booksLazyState,
                booksInfo = booksInfoUiState,
                openBookListener = openBookListener
            )
        }
    }
}
