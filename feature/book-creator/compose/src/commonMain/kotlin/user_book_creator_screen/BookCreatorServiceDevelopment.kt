package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.service_development_info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookCreatorServiceDevelopment(
    serviceDevelopment: Boolean,
    serviceDevelopmentCallback: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tooltipState = rememberTooltipState(isPersistent = true)
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    title = {
                        Text(
                            text = "О развитии сервиса",
                            style = ApplicationTheme.typography.buttonBold,
                            color = ApplicationTheme.colors.mainTextColor,
                        )
                    },
                    action = {
                    },
                    colors = RichTooltipColors(
                        containerColor = ApplicationTheme.colors.mainBackgroundColor,
                        contentColor = ApplicationTheme.colors.mainBackgroundColor,
                        titleContentColor = ApplicationTheme.colors.mainBackgroundColor,
                        actionContentColor = ApplicationTheme.colors.mainBackgroundColor,
                    ),
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Text(
                        text = stringResource(Res.string.service_development_info),
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.mainTextColor,
                    )
                }
            },
            state = tooltipState,
        ) {
            IconButton(
                onClick = { scope.launch { tooltipState.show() } }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = ApplicationTheme.colors.screenColor.activeLinkColor
                )
            }
        }

        Text(
            text = "Участвовать в развитии сервиса",
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainTextColor,
            textAlign = TextAlign.Start,
        )

        Switch(
            checked = serviceDevelopment,
            onCheckedChange = {
                serviceDevelopmentCallback()
            },
            modifier = Modifier.padding(horizontal = 8.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = ApplicationTheme.colors.screenColor.activeLinkColor,
                checkedTrackColor = ApplicationTheme.colors.screenColor.activeLinkColor,
            )
        )

    }
}