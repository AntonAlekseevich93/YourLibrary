package profile.elements

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import buttons.MenuButton
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.account
import yourlibrary.common.resources.generated.resources.administration_panel
import yourlibrary.common.resources.generated.resources.ic_moderation_menu
import yourlibrary.common.resources.generated.resources.logout_from_account

@Composable
internal fun AccountMenu(
    showAdminPanelMenu: Boolean,
    onAdminPanelClick: () -> Unit
) {
    Column {
        Text(
            text = stringResource(Res.string.account),
            style = ApplicationTheme.typography.title2Bold,
            color = ApplicationTheme.colors.textFieldColor.unfocusedLabelColor,
            modifier = Modifier.padding(bottom = 8.dp, top = 36.dp, start = 16.dp)
        )
        if (showAdminPanelMenu) {
            MenuButton(
                icon = Res.drawable.ic_moderation_menu,
                iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                invoke = {
                    Text(
                        text = stringResource(Res.string.administration_panel),
                        style = ApplicationTheme.typography.headlineRegular,
                        color = ApplicationTheme.colors.mainTextColor,

                        )
                },
                onClick = onAdminPanelClick
            )
        }

        MenuButton(
            icon = Res.drawable.ic_moderation_menu,
            iconColorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
            invoke = {
                Text(
                    text = stringResource(Res.string.logout_from_account),
                    style = ApplicationTheme.typography.headlineRegular,
                    color = ApplicationTheme.colors.mainTextColor,

                    )
            },
            showDivider = false,
            onClick = {}
        )
    }
}