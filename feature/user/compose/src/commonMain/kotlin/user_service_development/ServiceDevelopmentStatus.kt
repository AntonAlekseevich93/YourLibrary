package user_service_development

import ApplicationTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.approved
import yourlibrary.common.resources.generated.resources.await_moderation
import yourlibrary.common.resources.generated.resources.discard

@Composable
internal fun ServiceDevelopmentStatus(
    isModerated: Boolean,
    isApproved: Boolean,
) {
    var color = ApplicationTheme.colors.serviceDevelopmentStatusColor.awaitColor
    var textRes = Res.string.await_moderation
    if (isApproved) {
        textRes = Res.string.approved
        color = ApplicationTheme.colors.serviceDevelopmentStatusColor.approvedColor
    } else if (isModerated) {
        textRes = Res.string.discard
        color = ApplicationTheme.colors.serviceDevelopmentStatusColor.discardColor
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(textRes),
            style = ApplicationTheme.typography.footnoteRegular,
            color = ApplicationTheme.colors.mainBackgroundColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
