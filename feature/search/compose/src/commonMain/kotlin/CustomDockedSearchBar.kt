import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import di.Inject
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CustomDockedSearchBar(
    showSearch: State<Boolean>,
    closeSearch: () -> Unit,
) {
    val viewModel = remember { Inject.instance<SearchViewModel>() }
    val uiState by viewModel.uiState.collectAsState()
    val searchText = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    AnimatedVisibility(
        visible = showSearch.value,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium))
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            closeSearch.invoke()
                        },
                    )
                }
        ) {
            DockedSearchBar(
                modifier = Modifier
                    .padding(top = 64.dp)
                    .widthIn(min = 700.dp)
                    .heightIn(min = 10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                /** это нужно чтобы перехватывать onPress
                                 * на корневом Box который закрывает поиск*/
                            },
                        )
                    }
                    .focusRequester(focusRequester),
                query = searchText.value,
                placeholder = {
                    Text(
                        text = Strings.fast_search,
                        style = ApplicationTheme.typography.title3Medium,
                        color = ApplicationTheme.colors.searchIconColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                leadingIcon = {
                    Image(
                        painter = painterResource(Drawable.drawable_ic_search_glass),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                        modifier = Modifier.size(20.dp)

                    )
                },
                trailingIcon = {
                    if (searchText.value.isNotEmpty()) {
                        Image(
                            painter = painterResource(Drawable.drawable_ic_close),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                            modifier = Modifier.size(20.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    searchText.value = ""
                                    viewModel.clearSearch()
                                }
                            )
                        )
                    }
                },
                colors = SearchBarDefaults.colors(
                    containerColor = ApplicationTheme.colors.searchBackground,
                    inputFieldColors = TextFieldDefaults.colors(focusedTextColor = Color.White)
                ),
                active = true,
                shape = RoundedCornerShape(8.dp),
                onActiveChange = {

                },
                onQueryChange = {
                    searchText.value = it
                    viewModel.search(
                        it,
                        style = SpanStyle(color = Color(0xFFffa62b))
                    )
                },
                onSearch = {
                    //todo вызывается когда жмем интер
                },
                enabled = true
            ) {
                SearchInfo(
                    isSearchingProcess = uiState.isSearchingProcess,
                    emptySearchState = uiState.isEmptySearch,
                    showRecentSearchList = uiState.showRecentSearchList,
                    recentSearchList = uiState.recentSearchList,
                    searchedTasks = uiState.searchedTasks,
                    searchTextCallback = {
                        searchText.value = it
                        viewModel.search(
                            it,
                            style = SpanStyle(color = Color.Red)
                        )
                    }
                )
            }
        }
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
    }
}