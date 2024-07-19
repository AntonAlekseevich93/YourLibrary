import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import models.ShelfBoardsEvents
import platform.Platform
import platform.isDesktop


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShelfBoardScreen(
    platform: Platform,
    viewModel: ShelfViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val verticalPadding: Int = remember { if (platform.isDesktop()) 0 else 6 }
    val horizontalPadding: Int = remember { if (platform.isDesktop()) 24 else 10 }
    val lazyListState = rememberLazyListState()
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.value.isRefreshingState,
        onRefresh = {
            if (!uiState.value.isRefreshingState) {
                viewModel.sendEvent(ShelfBoardsEvents.OnDataRefresh)
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.sendEvent(
            ShelfBoardsEvents.SetBottomSheetExpandListener {
                scope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        BottomSheetScaffold(
            sheetPeekHeight = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            scaffoldState = bottomSheetState,
            sheetShape = RoundedCornerShape(14.dp),
            sheetContent = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    viewModel.FullShelfScreen(
                        platform = platform,
                        bookList = uiState.value.sortBookList,
                        config = uiState.value.config,
                        index = uiState.value.fullShelfIndex,
                        searchListener = viewModel::searchInShelf,
                        closeListener = {
                            scope.launch {
                                bottomSheetState.bottomSheetState.collapse()
                            }
                        },
                    )
                }
            },
            backgroundColor = ApplicationTheme.colors.mainBackgroundColor,
        ) {
            Box(
                modifier = Modifier.padding(
                    vertical = verticalPadding.dp,
                    horizontal = horizontalPadding.dp
                )
            ) {
                LazyColumn(state = lazyListState) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PullRefreshIndicator(
                                uiState.value.isRefreshingState,
                                pullRefreshState,
                                backgroundColor = ApplicationTheme.colors.mainIconsColor.copy(alpha = 0.8f)
                            )
                            AnimatedVisibility(uiState.value.isRefreshingState) {
                                Spacer(modifier = Modifier.padding(bottom = 24.dp))
                            }
                        }
                    }

                    itemsIndexed(uiState.value.shelvesList) { index, item ->
                        viewModel.HorizontalShelfScreen(
                            shelfVo = item,
                            config = uiState.value.config,
                            index = index,
                        )
                        //todo нужно добавить зероскрин когда у нас нету книг
                    }
                }
            }
        }
    }
}