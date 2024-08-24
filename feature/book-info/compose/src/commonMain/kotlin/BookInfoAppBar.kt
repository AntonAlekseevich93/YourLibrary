import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookCreatorAppBar(
    title: String,
    transparentAppbar: State<Boolean>,
    showBackButton: State<Boolean>,
    onClose: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 85.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AppBarComponent(
            title = title,
            transparentAppbar = transparentAppbar,
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
    transparentAppbar: State<Boolean>,
    showBackButton: State<Boolean>,
    onClose: () -> Unit,
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
                    if (showBackButton.value) {
                        Card(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = if (transparentAppbar.value)
                                    Color.Transparent
                                else ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.5f)
                            )
                        ) {
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
                        }
                    } else {
                        Spacer(Modifier.padding(start = 32.dp))
                    }
                },
                actions = {
                    Card(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = if (transparentAppbar.value)
                                Color.Transparent
                            else ApplicationTheme.colors.mainBackgroundColor.copy(alpha = 0.5f)
                        )
                    ) {
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
                }
            )
        }
    }
}