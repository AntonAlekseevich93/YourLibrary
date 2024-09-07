import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
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
fun BooksListInfoAppBar(
    hazeBlurState: HazeState,
    title: String,
    showBackButton: Boolean,
    isHazeBlurEnabled: Boolean,
    onBack: () -> Unit,
    onClose: () -> Unit,
) {

    var modifier = Modifier.fillMaxWidth().sizeIn(minHeight = 85.dp)
    val shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 8.dp)

    modifier = if (isHazeBlurEnabled) {
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
        AppBarComponent(
            title = title,
            showBackButton = showBackButton,
            onClose = onClose,
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBarComponent(
    title: String,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onClose: () -> Unit,
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
                        Modifier.fillMaxWidth().background(Color.Transparent)
                            .padding(start = 16.dp),
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
                    IconButton(
                        onClick = {
                            onClose.invoke()
                        },
                        modifier = Modifier
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    }
}