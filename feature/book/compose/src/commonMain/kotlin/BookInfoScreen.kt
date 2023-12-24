import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.kamel.core.Resource
import main_models.BookItemVo
import navigation_drawer.PlatformNavigationDrawer
import navigation_drawer.PlatformRightDrawerContent
import navigation_drawer.SelectedRightDrawerItem
import platform.Platform
import tooltip_area.TooltipItem

@Composable
fun BookInfoScreen(
    platform: Platform,
    bookItem: BookItemVo,
    fullScreenNote: MutableState<Boolean>,
    painterInCache: Resource<Painter>? = null,
    showLeftDrawer: State<Boolean>,
    showRightDrawer: MutableState<Boolean>,
    openLeftDrawerListener: () -> Unit,
    openRightDrawerListener: () -> Unit,
    closeRightDrawerListener: () -> Unit,
    tooltipCallback: ((tooltip: TooltipItem) -> Unit),
    onClose: () -> Unit,
) {

    val targetVerticalPadding = if (fullScreenNote.value) 0.dp else 65.dp
    val targetHorizontalPadding =
        if (fullScreenNote.value || platform == Platform.MOBILE) 0.dp else if (showRightDrawer.value) 100.dp else 220.dp
    val animatedVerticalPadding by animateDpAsState(
        targetValue = targetVerticalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val animatedHorizontalPadding by animateDpAsState(
        targetValue = targetHorizontalPadding,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = 10,
            easing = FastOutSlowInEasing
        )
    )
    val shapeInDp = if (fullScreenNote.value) 0.dp else 8.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.8f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClose.invoke()
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(shapeInDp),
            colors = CardDefaults.cardColors(
                containerColor = ApplicationTheme.colors.mainBackgroundWindowDarkColor
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = animatedVerticalPadding,
                    horizontal = animatedHorizontalPadding
                )

                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            /** это нужно чтобы перехватывать onPress
                             * на корневом Box который закрывает поиск*/
                        },
                    )
                },
        ) {
            PlatformNavigationDrawer(
                platform = platform,
                rightDrawerContent = {
                    AnimatedVisibility(visible = showRightDrawer.value) {
                        Row {
                            Divider(
                                modifier = Modifier.fillMaxHeight().width(1.dp),
                                thickness = 1.dp,
                                color = ApplicationTheme.colors.divider
                            )
                            PlatformRightDrawerContent(
                                platform = platform,
                                isFullscreen = fullScreenNote,
                                isCanClose = true,
                                selectedItem = SelectedRightDrawerItem.STRUCTURE,
                                closeSidebarListener = closeRightDrawerListener,
                                expandOrCollapseListener = {
                                    fullScreenNote.value = !fullScreenNote.value
                                },
                                closeWindow = onClose,
                                tooltipCallback = tooltipCallback
                            )
                        }
                    }
                },
                background = ApplicationTheme.colors.mainBackgroundWindowDarkColor,
                showRightDrawer = showRightDrawer
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FullBookBar(
                        platform = platform,
                        showLeftDrawer = showLeftDrawer,
                        showRightDrawer = showRightDrawer,
                        onClose = onClose,
                        onFullscreen = { fullScreenNote.value = !fullScreenNote.value },
                        isFullscreen = fullScreenNote,
                        openLeftDrawerListener = openLeftDrawerListener,
                        openRightDrawerListener = openRightDrawerListener,
                        tooltipCallback = tooltipCallback
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth().height(1.dp),
                        color = ApplicationTheme.colors.divider
                    )
                    BookInfoContent(
                        platform = platform,
                        bookItem = bookItem,
                        painterInCache = painterInCache,
                    )
                }
            }
        }
    }
}