package animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import containters.CenterColumnContainerMaxSize
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import yourlibrary.common.resources.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ConfettiAnimation(show: State<Boolean>, finishAnimation: () -> Unit) {
    val scope = rememberCoroutineScope()
    AnimatedVisibility(
        show.value, enter = scaleIn(animationSpec = tween()),
        exit = scaleOut()
    ) {
        val composition: LottieCompositionResult = rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes("files/lottie_confetti.json").decodeToString()
            )
        }

        val progress by animateLottieCompositionAsState(composition.value)
        LaunchedEffect(composition.isComplete) {
            if (composition.isComplete) {
                scope.launch {
                    delay(3300)
                    finishAnimation()
                }
            }
        }

        CenterColumnContainerMaxSize {
            Image(
                painter = rememberLottiePainter(
                    composition = composition.value,
                    progress = {
                        progress
                    },
                ),
                contentDescription = null,
            )
        }
    }
}