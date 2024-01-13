import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import di.Inject
import io.kamel.core.Resource
import kotlinx.coroutines.launch
import platform.Platform
import platform.isDesktop


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShelfBoardScreen(
    platform: Platform,
    openBookListener: (painterSelectedBookInCache: Resource<Painter>?, bookId: String) -> Unit,
) {
    val viewModel = remember { Inject.instance<ShelfViewModel>() }
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

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            sheetPeekHeight = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            scaffoldState = bottomSheetState,
            sheetShape = RoundedCornerShape(14.dp),
            sheetContent = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    FullShelfScreen(
                        platform = platform,
                        bookList = uiState.value.sortBookList.value,
                        config = uiState.value.config,
                        index = uiState.value.fullShelfIndex.value,
                        searchListener = viewModel::searchInShelf,
                        openBookListener = openBookListener,
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
                    itemsIndexed(uiState.value.shelfList.value) { index, item ->
                        HorizontalShelfScreen(
                            shelfVo = item,
                            config = uiState.value.config,
                            expandShelfListener = {
                                viewModel.showFullShelf(index)
                                scope.launch {
                                    bottomSheetState.bottomSheetState.expand()
                                }
                            },
                            openBookListener = openBookListener
                        )
                        //todo нужно добавить зероскрин когда у нас нету книг
                    }
                }
            }
        }
    }
}