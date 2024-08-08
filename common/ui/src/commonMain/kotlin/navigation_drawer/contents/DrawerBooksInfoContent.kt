package navigation_drawer.contents

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import main_models.BookVo
import main_models.BooksInfoHeader
import navigation_drawer.contents.models.DrawerEvents


@Composable
fun BaseEventScope<BaseEvent>.DrawerBooksInfoContent(
    booksLazyState: LazyListState,
    booksInfo: SnapshotStateMap<BooksInfoHeader, SnapshotStateList<BookVo>>,
) {
    /**don`t use padding in LazyColumn because items uses hovered **/
    LazyColumn(state = booksLazyState) {
        item {
            Spacer(modifier = Modifier.padding(top = 10.dp))
        }

        booksInfo.keys.sortedBy { it.priorityInList }.forEachIndexed { index, item ->
            if (booksInfo[item]?.isNotEmpty() == true) {
                item {
                    val rotation by animateFloatAsState(
                        targetValue = if (item.isCollapse.value) 0f else 90f,
                    )

                    val iconModifier = if (item.isCollapse.value)
                        Modifier.padding(end = 10.dp).size(12.dp)
                    else {
                        Modifier.padding(end = 10.dp).size(12.dp).rotate(rotation)
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                            .clickable(interactionSource = MutableInteractionSource(), null) {
                                item.isCollapse.value = !item.isCollapse.value
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = ApplicationTheme.colors.mainIconsColor,
                            modifier = iconModifier
                        )

                        Text(
                            text = item.name,
                            style = ApplicationTheme.typography.buttonBold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                    }
                }
            }

            booksInfo[item]?.toList()?.let {
                if (!item.isCollapse.value) {
                    items(it) { book ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isHovered = interactionSource.collectIsHoveredAsState()
                        val cardBackground = if (isHovered.value) {
                            ApplicationTheme.colors.pointerIsActiveCardColor
                        } else {
                            Color.Transparent
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(interactionSource, null) {
                                    book.localId?.let {
                                        this@DrawerBooksInfoContent.sendEvent(
                                            DrawerEvents.OpenBook(it)
                                        )
                                    }
                                }
                                .hoverable(
                                    interactionSource = interactionSource,
                                    enabled = true,
                                ),
                            shape = RoundedCornerShape(0.dp),
                            colors = CardDefaults.cardColors(cardBackground)
                        ) {
                            Text(
                                text = book.bookName,
                                modifier = Modifier.padding(
                                    start = 46.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                ),
                                style = ApplicationTheme.typography.footnoteRegular.copy(fontSize = 13.sp),
                                color = ApplicationTheme.colors.mainTextColor,
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    }
}
