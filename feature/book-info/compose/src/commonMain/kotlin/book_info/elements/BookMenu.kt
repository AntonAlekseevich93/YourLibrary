package book_info.elements

import ApplicationTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_share

@Composable
internal fun BookMenu(
    showDateSelectorDialog: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    AnimatedVisibility(
        !showMenu,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        CenterBoxContainer {
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        showMenu = true
                    }
                    .size(22.dp)
            )
        }
    }

    AnimatedVisibility(
        showMenu,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
            ) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Image(
                        painter = painterResource(Res.drawable.ic_share),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                        modifier = Modifier.size(24.dp)
                            .clickable(MutableInteractionSource(), null) {
                            }
                    )

                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = ApplicationTheme.colors.mainIconsColor,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                showDateSelectorDialog()
                            }
                            .size(24.dp)
                    )

                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = ApplicationTheme.colors.mainIconsColor,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                showMenu = false
                            }
                            .size(24.dp)
                    )
                }
            }
        }
    }
}