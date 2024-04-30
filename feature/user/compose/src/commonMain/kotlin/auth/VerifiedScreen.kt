package auth

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.InfoBlock
import models.UserEvents
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import tags.CustomTag

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.VerifiedScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(DrawableResource(Drawable.drawable_app_logo)),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        InfoBlock(
            text = Strings.info_block_verify_user,
            textStyle = ApplicationTheme.typography.bodyRegular,
            background = ApplicationTheme.colors.cardBackgroundDark,
            modifier = Modifier.padding(bottom = 36.dp),
        )

        CustomTag(
            text = Strings.refresh,
            textStyle = ApplicationTheme.typography.bodyRegular,
            maxHeight = 36.dp
        ) {
            this@VerifiedScreen.sendEvent(UserEvents.GetUserIfVerified)
        }
    }
}