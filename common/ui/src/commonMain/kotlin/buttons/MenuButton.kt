package buttons

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MenuButton(
    icon: DrawableResource,
    iconSize: Dp = 20.dp,
    iconColorFilter: ColorFilter? = null,
    showDivider: Boolean = true,
    invoke: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Column {
        Spacer(Modifier.padding(top = 6.dp))
        Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
            Spacer(Modifier.padding(bottom = 6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    colorFilter = iconColorFilter,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(iconSize)
                )
                invoke()
                Spacer(Modifier.padding(16.dp))
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp).graphicsLayer(
                        rotationZ = 180f
                    ),
                    tint = ApplicationTheme.colors.mainIconsColor.copy(alpha = 0.8f)
                )
            }
            Spacer(Modifier.padding(top = 6.dp))
        }
        Spacer(Modifier.padding(top = 6.dp))
        if (showDivider) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp, end = 22.dp),
                thickness = 1.dp,
                color = ApplicationTheme.colors.hintColor.copy(alpha = 0.5f)
            )
        }
    }
}