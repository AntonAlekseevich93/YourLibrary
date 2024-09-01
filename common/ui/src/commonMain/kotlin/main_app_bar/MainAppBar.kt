package main_app_bar

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import main_models.BookVo
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.app_name
import yourlibrary.common.resources.generated.resources.search_app_bar_text_field_hint

@Composable
fun BaseEventScope<BaseEvent>.MainAppBar(
    hazeBlurState: HazeState,
    searchedBooks: List<BookVo>,
    platformDisplayHeight: Dp?,
    showSearchAppBarTextField: State<Boolean>,
    isHazeBlurEnabled: Boolean,
    changeVisibilitySearchAppBarTextField: () -> Unit,
) {
    var showSearchResult by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val targetHeight = with(density) {
        when {
            showSearchAppBarTextField.value && !showSearchResult -> 260.dp.toPx()
            showSearchResult && showSearchAppBarTextField.value -> {
                platformDisplayHeight?.toPx() ?: 260.dp.toPx()
            }

            else -> 85.dp.toPx()
        }
    }
    val animatedHeight = remember { with(density) { Animatable(85.dp.toPx()) } }
    var appBarModifier = Modifier.fillMaxWidth()
        .height(with(density) { animatedHeight.value.toDp() })
        .pointerInput(Unit) {
            //need for blocking scroll on screen
        }

    val shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 8.dp)

    appBarModifier = if (isHazeBlurEnabled) {
        appBarModifier.hazeChild(
            state = hazeBlurState,
            shape = shape
        )
    } else {
        appBarModifier.clip(shape).background(ApplicationTheme.colors.mainBackgroundColor)
    }

    LaunchedEffect(targetHeight) {
        animatedHeight.animateTo(
            targetValue = targetHeight,
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }
    Column(
        modifier = appBarModifier,
        verticalArrangement = Arrangement.Top
    ) {
        AppBarComponent(showSearchAppBarTextField.value, changeVisibilitySearchAppBarTextField)
        Spacer(Modifier.padding(top = 16.dp))
        SearchAppBarTextField(
            hintText = stringResource(Res.string.search_app_bar_text_field_hint),
            modifier = Modifier.padding(horizontal = 16.dp),
            onTextChanged = {
                if (it.isNotEmpty() && !showSearchResult) {
                    showSearchResult = true
                } else if (it.isEmpty() && showSearchResult) {
                    showSearchResult = false
                }
                this@MainAppBar.sendEvent(MainAppBarEvents.OnSearch(it))
            }
        )
        SearchAppBarResultInfo(searchedBooks)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBarComponent(isSearchState: Boolean, showListener: () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides Color.White
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth().background(Color.Transparent)
                            .padding(start = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.app_name),
                            color = Color.White,
                            style = ApplicationTheme.typography.appTitle
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Spacer(Modifier.padding(start = 32.dp))
                },
                actions = {
                    IconButton(
                        onClick = {
                            showListener.invoke()
                        },
                        modifier = Modifier
                    ) {
                        AnimatedVisibility(!isSearchState) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                            )
                        }
                        AnimatedVisibility(isSearchState) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }
    }
}