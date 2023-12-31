import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import application_platform.ApplicationPlatform
import di.PlatformConfiguration
import java.awt.FileDialog
import java.awt.Frame

fun main() = application {
    PlatformSDK.init(PlatformConfiguration())
    val state = rememberWindowState(
        size = DpSize(1300.dp, 900.dp),
        position = WindowPosition(Alignment.Center)
    )

    val isFullScreen = remember { mutableStateOf(false) }
    val windowCloseListener = ::exitApplication

    if (isFullScreen.value) {
        val fullScreenState = rememberWindowState(
            placement = WindowPlacement.Fullscreen,
            size = state.size,
            position = state.position
        )
        Window(
            title = "YourLibrary",
            onCloseRequest = windowCloseListener,
            undecorated = false,
            transparent = false,
            state = fullScreenState
        ) {
            Column {
                if (fullScreenState.placement == WindowPlacement.Floating) {
                    isFullScreen.value = false
                }
                Application(ApplicationPlatform.DESKTOP)
            }
        }
    } else {
        Window(
            onCloseRequest = windowCloseListener,
            undecorated = true,
            transparent = true,
            state = state
        ) {
            if (state.isMinimized) {
                state.isMinimized = false
            }
            Card(shape = RoundedCornerShape(8.dp)) {
                Column() {
                    AppWindowTitleBar(
                        closeListener = {
                            windowCloseListener.invoke()
                        },
                        isMaximizedListener = {
                            state.isMinimized = true
                        },
                        fullscreenListener = {
                            isFullScreen.value = true
                        }
                    )
                    Application(ApplicationPlatform.DESKTOP)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Button(
    iconRes: String,
    iconSize: Dp = 12.dp,
    color: Color = Color(210, 210, 210),
    size: Int = 12,
    modifier: Modifier = Modifier,
    showIconState: Boolean,
    showIconCallback: (isShowing: Boolean) -> Unit,
    onClick: () -> Unit = {},
) {

    val inActiveColor = Color(0xFF757575)
    val buttonHover = remember { mutableStateOf(false) }
    Surface(
        color = if (buttonHover.value)
            color
        else
            color,
        shape = RoundedCornerShape((size / 2).dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable(onClick = onClick)
                .size(size.dp, size.dp)
                .onPointerEvent(PointerEventType.Move) {
                    buttonHover.value = true
                    showIconCallback.invoke(true)
                }
                .onPointerEvent(PointerEventType.Enter) {
                    buttonHover.value = true
                }
                .onPointerEvent(PointerEventType.Exit) {
                    buttonHover.value = false
                    showIconCallback.invoke(false)
                }
        ) {
            if (showIconState) {
                Image(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    }
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

@Composable
private fun WindowScope.AppWindowTitleBar(
    closeListener: () -> Unit,
    isMaximizedListener: () -> Unit,
    fullscreenListener: () -> Unit,
) = WindowDraggableArea {
    val showIconState = remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth().height(48.dp).background(Color(0xFF2E2E2E))) {
        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp, start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                iconRes = drawable_close_icon,
                showIconState = showIconState.value,
                color = Color(0xFFFF5F58),
                modifier = Modifier.padding(end = 8.dp),
                onClick = closeListener,
                showIconCallback = { showIconState.value = it }
            )
            Button(
                iconRes = drawable_min,
                iconSize = 10.dp,
                showIconState = showIconState.value,
                color = Color(0xFFFEBC2E),
                modifier = Modifier.padding(end = 8.dp),
                onClick = isMaximizedListener,
                showIconCallback = { showIconState.value = it }
            )
            Button(
                iconRes = drawable_ico,
                showIconState = showIconState.value,
                color = Color(0xFF29C940),
                onClick = fullscreenListener,
                showIconCallback = { showIconState.value = it }
            )
        }
    }
}