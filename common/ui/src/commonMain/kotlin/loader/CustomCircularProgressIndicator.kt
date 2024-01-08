package loader

import ApplicationTheme
import Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main_models.rest.LoadingStatus
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingStatusIndicator(
    loadingStatus: MutableState<LoadingStatus>,
    color: Color = ApplicationTheme.colors.linkColor,
    trackColor: Color = ApplicationTheme.colors.cardBackgroundDark,
    finishAnimationListener: () -> Unit,
) {
    val width: Dp =
        remember { 64.dp } //todo если мы хотим изменять размер нужно подумать как расчитывать размеры и отступы для иконки
    val scope = rememberCoroutineScope()
    val showIconAnimation = remember {
        mutableStateOf(false)
    }

    val progressLoading = remember {
        mutableStateOf(0f)
    }

    @Composable
    fun getSuccessOrErrorColor(): Color = if (loadingStatus.value == LoadingStatus.SUCCESS) {
        ApplicationTheme.colors.successColor
    } else {
        ApplicationTheme.colors.errorColor
    }

    LaunchedEffect(key1 = loadingStatus.value) {
        if (loadingStatus.value != LoadingStatus.LOADING) {
            scope.launch {
                delay(200)
                showIconAnimation.value = true
            }
            scope.launch {
                while (progressLoading.value != 1f) {
                    delay(5)
                    progressLoading.value = progressLoading.value + 0.006f
                }
            }
        }
    }

    CenterBoxContainer(modifier = Modifier) {
        if (loadingStatus.value == LoadingStatus.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier.width(width),
                color = if (showIconAnimation.value) {
                    getSuccessOrErrorColor()
                } else color,
                trackColor = trackColor
            )
        }

        if (loadingStatus.value != LoadingStatus.LOADING) {
            CircularProgressIndicator(
                progress = progressLoading.value,
                modifier = Modifier.width(width),
                color = getSuccessOrErrorColor(),
                trackColor = trackColor,
            )
        }

        Column(modifier = Modifier.size(64.dp).padding(top = 24.dp, start = 12.dp)) {
            AnimatedVisibility(
                showIconAnimation.value,
                enter = scaleIn(animationSpec = tween(durationMillis = 1200)),
                exit = scaleOut()
            ) {
                if (loadingStatus.value == LoadingStatus.SUCCESS) {
                    Image(
                        painter = painterResource(Drawable.drawable_ic_check),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ApplicationTheme.colors.successColor),
                        modifier = Modifier
                    )
                }
                if (loadingStatus.value == LoadingStatus.ERROR) {
                    Image(
                        painter = painterResource(Drawable.drawable_ic_close_128px),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ApplicationTheme.colors.errorColor),
                        modifier = Modifier
                    )
                }
                scope.launch {
                    delay(2500)
                    finishAnimationListener.invoke()
                }
            }
        }
    }
}
