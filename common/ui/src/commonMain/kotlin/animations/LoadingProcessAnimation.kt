package animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import containters.CenterColumnContainerMaxSize
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import yourlibrary.common.resources.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingProcessAnimation(show: State<Boolean>) {
    AnimatedVisibility(
        show.value, enter = scaleIn(animationSpec = tween()),
        exit = scaleOut()
    ) {
        val composition: LottieCompositionResult = rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes("files/lottie_loading_process.json").decodeToString()
            )
        }

        val progress by animateLottieCompositionAsState(composition.value)

        CenterColumnContainerMaxSize {
            Image(
                painter = rememberLottiePainter(
                    composition = composition.value,
                    progress = {
                        progress
                    },
                ),
                contentDescription = null,
                modifier = Modifier.sizeIn(maxWidth = 220.dp, maxHeight = 220.dp)
            )
        }
    }
}