package bottom_app_bar

import ApplicationTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import navigation.MenuItem
import navigation.RootComponent

@Composable
fun CustomBottomBar(
    hazeState: HazeState,
    isHazeBlurEnabled: Boolean,
    navigationComponent: RootComponent,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(16.dp)

    var modifier: Modifier = Modifier
        .padding(vertical = 16.dp, horizontal = 16.dp)
        .fillMaxWidth()
        .height(64.dp)

    modifier = if (isHazeBlurEnabled) {
        modifier.hazeChild(state = hazeState, shape = shape)
    } else {
        modifier.clip(shape).background(ApplicationTheme.colors.mainBackgroundColor)
    }

    Column(Modifier.background(Color.Transparent)) {
        Box(
            modifier = modifier
                .border(
                    width = Dp.Hairline,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8b8c89).copy(alpha = .8f),
                            Color.White.copy(alpha = .2f),
                        ),
                    ),
                    shape = shape
                )
        ) {
            BottomBarTabs(
                tabs = bottomBarTabs,
                selectedTab = selectedTabIndex,
                onTabSelected = { tab ->
                    selectedTabIndex = bottomBarTabs.indexOf(tab)
                    when (tab) {
                        is BottomBarTab.Creator -> {
                            navigationComponent.menuClick(MenuItem.CREATOR)
                        }

                        is BottomBarTab.Profile -> {
                            navigationComponent.menuClick(MenuItem.PROFILE)
                        }

                        is BottomBarTab.Home -> {
                            navigationComponent.menuClick(MenuItem.MAIN)
                        }

                        is BottomBarTab.Settings -> {
                            navigationComponent.menuClick(MenuItem.SETTINGS)
                        }
                    }
                }
            )
            val animatedSelectedTabIndex by animateFloatAsState(
                targetValue = selectedTabIndex.toFloat(),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                )
            )
            val animatedColor by animateColorAsState(
                targetValue = bottomBarTabs[selectedTabIndex].color,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                )
            )
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            ) {
                val tabWidth = size.width / bottomBarTabs.size
                drawCircle(
                    color = animatedColor.copy(alpha = .6f),
                    radius = size.height / 2,
                    center = Offset(
                        (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                        size.height / 2
                    )
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            ) {
                val path = Path().apply {
                    addRoundRect(RoundRect(size.toRect(), CornerRadius(20.dp.toPx())))
                }
                val length = PathMeasure().apply { setPath(path, false) }.length
                val tabWidth = size.width / bottomBarTabs.size
                drawPath(
                    path,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            animatedColor.copy(alpha = 0f),
                            animatedColor.copy(alpha = 1f),
                            animatedColor.copy(alpha = 1f),
                            animatedColor.copy(alpha = 0f),
                        ),
                        startX = tabWidth * animatedSelectedTabIndex,
                        endX = tabWidth * (animatedSelectedTabIndex + 1),
                    ),
                    style = Stroke(
                        width = 6f,
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(length / 2, length)
                        )
                    )
                )
            }
        }
        Spacer(Modifier.padding(6.dp))
    }
}

@Composable
internal fun BottomBarTabs(
    tabs: List<BottomBarTab>,
    selectedTab: Int,
    onTabSelected: (BottomBarTab) -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        ),
        LocalContentColor provides Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            for (tab in tabs) {
                val alpha by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .35f,
                )
                val scale by animateFloatAsState(
                    targetValue = if (selectedTab == tabs.indexOf(tab)) 1f else .98f,
                    visibilityThreshold = .000001f,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                    ),
                )
                Column(
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                        .fillMaxHeight()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onTabSelected(tab)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(imageVector = tab.icon, contentDescription = null)
                    Text(text = tab.title)
                }
            }
        }
    }
}