package reading_status

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import main_models.ReadingStatus

@Composable
fun ReadingStatus.getStatusColor(): Color = when (this) {
    ReadingStatus.PLANNED -> ApplicationTheme.colors.readingStatusesColor.plannedStatusColor
    ReadingStatus.READING -> ApplicationTheme.colors.readingStatusesColor.readingStatusColor
    ReadingStatus.DONE -> ApplicationTheme.colors.readingStatusesColor.doneStatusColor
    ReadingStatus.DEFERRED -> ApplicationTheme.colors.readingStatusesColor.deferredStatusColor
}
