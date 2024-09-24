import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
fun BookCreatorAppBar(
    hazeBlurState: HazeState,
    title: String,
    isHazeBlurEnabled: Boolean,
    showBackButton: Boolean,
    appBarModifier: Modifier = Modifier,
    onBack: () -> Unit,
) {

    var modifier = appBarModifier.fillMaxWidth()

    modifier = if (isHazeBlurEnabled) {
        modifier.hazeChild(
            state = hazeBlurState,
            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 8.dp)
        )
    } else {
        modifier.clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 8.dp))
            .background(ApplicationTheme.colors.mainBackgroundColor)
    }

    Column(
        modifier = modifier,
    ) {
        AppBarComponent(
            title = title,
            onBack = onBack,
            showBackButton = showBackButton,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBarComponent(
    title: String,
    showBackButton: Boolean,
    onBack: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides Color.White
    ) {
        Column(
            modifier = Modifier
                .height(88.dp)
                .statusBarsPadding()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    if (showBackButton) {
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
                    } else {
                        Spacer(Modifier.padding(start = 32.dp))
                    }
                },
                actions = {
                    Spacer(Modifier.padding(start = 32.dp))
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}