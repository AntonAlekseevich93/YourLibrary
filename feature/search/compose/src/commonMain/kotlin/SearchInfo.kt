import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import items.RecentSearchedItem
import items.SearchedTask
import models.SearchTaskItemUiState

@Composable
internal fun SearchInfo(
    isSearchingProcess: State<Boolean>,
    emptySearchState: State<Boolean>,
    showRecentSearchList: State<Boolean>,
    recentSearchList: SnapshotStateList<String>,
    searchedTasks: SnapshotStateList<SearchTaskItemUiState>,
    searchTextCallback: (searchedText: String) -> Unit,
) {
    val needShowDivider = remember(searchedTasks.size) { searchedTasks.size > 0 }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            item {
                if (showRecentSearchList.value && recentSearchList.isNotEmpty()) {
                    Text(
                        text = Strings.recent.uppercase(),
                        style = ApplicationTheme.typography.bodyRegular,
                        color = ApplicationTheme.colors.searchIconColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 28.dp, top = 16.dp, bottom = 6.dp)
                    )
                }
            }
            items(recentSearchList) {
                if (showRecentSearchList.value) {
                    RecentSearchedItem(text = it,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                        innerModifier = Modifier.padding(10.dp),
                        onClick = { searchedText ->
                            searchTextCallback.invoke(searchedText)
                        })
                }
            }

            item {
                if (searchedTasks.isNotEmpty()) {
                    Text(
                        text = Strings.tasks.uppercase(),
                        style = ApplicationTheme.typography.bodyRegular,
                        color = ApplicationTheme.colors.searchIconColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 28.dp, top = 16.dp, bottom = 6.dp)
                    )
                }
            }
            itemsIndexed(searchedTasks) { index, item ->
                SearchedTask(
                    task = item,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                    innerModifier = Modifier.padding(10.dp),
                    onClick = {
                        //todo
                    })
                if (needShowDivider && index != searchedTasks.size - 1) {
                    Divider(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .padding(start = 66.dp, end = 36.dp),
                        color = ApplicationTheme.colors.searchDividerColor
                    )
                }
            }

            item {
                val strokeWidth = 5.dp
                if (isSearchingProcess.value) {
                    Box(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .drawBehind {
                                    drawCircle(
                                        Color(0xFFffa62b),
                                        radius = size.width / 2 - strokeWidth.toPx() / 2,
                                        style = Stroke(strokeWidth.toPx())
                                    )
                                },
                            color = ApplicationTheme.colors.searchIconColor,
                            strokeWidth = strokeWidth
                        )
                    }
                }
            }

            item {
                if (emptySearchState.value) {
                    EmptySearch()
                }
            }
        }
    }
}