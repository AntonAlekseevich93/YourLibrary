package user_book_creator_screen.elements

import ApplicationTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_lock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TextFieldWasLockedTooltipIcon(
    tooltipText: String,
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)

    LaunchedEffect(tooltipState.isVisible) {
        if (tooltipState.isVisible) {
            delay(3500)
            tooltipState.dismiss()
        }
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                colors = RichTooltipColors(
                    containerColor = ApplicationTheme.colors.mainBackgroundColor,
                    contentColor = ApplicationTheme.colors.mainBackgroundColor,
                    titleContentColor = ApplicationTheme.colors.mainBackgroundColor,
                    actionContentColor = ApplicationTheme.colors.mainBackgroundColor,
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text(
                    text = tooltipText,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(vertical = 18.dp)
                )
            }
        },
        state = tooltipState,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_lock),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
                .padding(end = 4.dp)
                .clickable(MutableInteractionSource(), null) {
                    scope.launch {
                        tooltipState.show()
                    }
                }
        )
    }
}