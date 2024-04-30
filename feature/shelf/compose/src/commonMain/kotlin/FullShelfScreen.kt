import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import main_models.BookItemVo
import models.BookItemCardConfig
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import platform.Platform
import platform.isDesktop

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.FullShelfScreen(
    platform: Platform,
    bookList: List<BookItemVo>,
    config: BookItemCardConfig,
    index: Int,
    searchListener: (text: String, shelfIndex: Int) -> Unit,
    closeListener: () -> Unit,
) {
    val searchText = remember { mutableStateOf("") }
    val lazyGridState = rememberLazyGridState()
    val showSearch = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    showSearch.value = lazyGridState.firstVisibleItemIndex <= 0

    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor)
    ) {
        Column(modifier = Modifier) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
                ),
            ) {
                AnimatedVisibility(showSearch.value) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 6.dp)
                    ) {
                        SearchBar(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            query = searchText.value,
                            onQueryChange = {
                                searchText.value = it
                                searchListener.invoke(
                                    it,
                                    index
                                )
                            },
                            onSearch = {},
                            active = false,
                            onActiveChange = {},
                            colors = SearchBarDefaults.colors(
                                containerColor = ApplicationTheme.colors.searchBackgroundDark,
                                inputFieldColors = TextFieldDefaults.colors(focusedTextColor = Color.White)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Image(
                                    painter = painterResource(DrawableResource(Drawable.drawable_ic_search_glass)),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                        ) {
                            //nop
                        }
                        if (platform.isDesktop()) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = ApplicationTheme.colors.mainIconsColor,
                                    modifier = Modifier.padding(end = 24.dp).size(26.dp).clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null,
                                        onClick = closeListener
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = !showSearch.value,
                        enter = fadeIn(),
                        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    ) {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    lazyGridState.animateScrollToItem(0)
                                }
                            },
                            containerColor = ApplicationTheme.colors.mainBackgroundColor,
                            elevation = FloatingActionButtonDefaults.elevation(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowCircleUp,
                                contentDescription = null,
                                tint = ApplicationTheme.colors.mainIconsColor,
                                modifier = Modifier
                            )
                        }
                    }
                },
                containerColor = Color.Transparent
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(145.dp),
                    modifier = Modifier.fillMaxSize(),
                    state = lazyGridState
                ) {
                    items(bookList) { bookItem ->
                        BookItemShelfCard(
                            config = config,
                            bookItem = bookItem,
                        )
                    }
                }
            }
        }
    }
}