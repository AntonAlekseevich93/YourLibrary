package auth.elements

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import models.UserEvents

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseEventScope<BaseEvent>.ButtonSwitcher(
    isSignUnState: State<Boolean>,
    buttonIsActive: State<Boolean>,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Chip(
            modifier = Modifier.sizeIn(maxWidth = 240.dp).padding(bottom = 4.dp).fillMaxWidth(),
            enabled = buttonIsActive.value,
            onClick = onClick,
            content = {
                CenterBoxContainer {
                    Text(
                        text = if (isSignUnState.value) Strings.signUp else Strings.signIn,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ChipDefaults.chipColors(
                backgroundColor = ApplicationTheme.colors.primaryButtonColor,
                disabledBackgroundColor = ApplicationTheme.colors.secondaryButtonColor,
            )
        )

        Row(
            modifier = Modifier
                .sizeIn(maxWidth = 240.dp)
                .fillMaxWidth()
                .padding(bottom = 4.dp, top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 2.dp),
                color = ApplicationTheme.colors.hintColor
            )
            Text(
                text = Strings.or,
                style = ApplicationTheme.typography.footnoteRegular,
                color = ApplicationTheme.colors.hintColor,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 2.dp),
                color = ApplicationTheme.colors.hintColor
            )
        }

        Chip(
            modifier = Modifier.sizeIn(maxWidth = 240.dp).fillMaxWidth(),
            onClick = {
                if (isSignUnState.value) {
                    this@ButtonSwitcher.sendEvent(UserEvents.OnSignInClick)
                } else {
                    this@ButtonSwitcher.sendEvent(UserEvents.OnSignUpClick)
                }
            },
            content = {
                CenterBoxContainer {
                    Text(
                        text = if (isSignUnState.value) {
                            Strings.signIn
                        } else Strings.signUp,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ChipDefaults.chipColors(
                backgroundColor = ApplicationTheme.colors.mainBackgroundColor
            )
        )
    }
}