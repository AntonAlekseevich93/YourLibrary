import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

@Composable
internal fun ReviewsAndRatingsAppBar(
    hazeBlurState: HazeState?,
    isHazeBlurEnabled: Boolean,
    title: String,
    onBack: () -> Unit,
) {
    val shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 8.dp)
    var modifier = Modifier
        .fillMaxWidth()
        .sizeIn(minHeight = 85.dp)

    modifier = if (isHazeBlurEnabled && hazeBlurState != null) {
        modifier.hazeChild(
            state = hazeBlurState,
            shape = shape
        )
    } else {
        modifier.clip(shape).background(ApplicationTheme.colors.mainBackgroundColor)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        UserCommonAppBarComponent(
            title = title,
            onBack = onBack,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserCommonAppBarComponent(
    title: String,
    onBack: () -> Unit,
) {
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
                        Modifier.fillMaxWidth().background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            style = ApplicationTheme.typography.appTitle
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBack.invoke()
                        },
                        modifier = Modifier
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    Spacer(Modifier.padding(start = 32.dp))
                }
            )
        }
    }
}