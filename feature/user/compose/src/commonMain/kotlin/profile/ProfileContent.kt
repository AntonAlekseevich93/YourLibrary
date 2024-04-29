package profile

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import common.HoveredTextWithIcon
import main_models.user.UserVo
import models.UserEvents
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import profile.elements.Title

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.ProfileContent(userInfo: State<UserVo>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
    ) {
        Text(
            text = userInfo.value.name,
            style = ApplicationTheme.typography.title1Bold,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(top = 16.dp, start = 24.dp)
        )

        Divider(
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            thickness = 1.dp,
            color = ApplicationTheme.colors.dividerLight
        )

        Title(text = Strings.email, modifier = Modifier.padding(start = 24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(Drawable.drawable_ic_email),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                modifier = Modifier.size(54.dp).padding(end = 6.dp, start = 24.dp)
            )

            Text(
                text = userInfo.value.email,
                style = ApplicationTheme.typography.headlineRegular,
                color = ApplicationTheme.colors.mainTextColor
            )
        }

        Spacer(Modifier.padding(16.dp))

        Title(text = Strings.account, modifier = Modifier.padding(start = 24.dp))

        HoveredTextWithIcon(
            iconRes = Drawable.drawable_ic_exit,
            title = Strings.logout_from_account,
            onClick = {
                this@ProfileContent.sendEvent(UserEvents.OnSignOut)
            }
        )
    }
}