package main_models

import Strings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ReadingStatus(val nameValue: String) {
    DEFERRED(Strings.reading_status_deferred),
    PLANNED(Strings.reading_status_planned),
    READING(Strings.reading_status_is_reading),
    DONE(Strings.reading_status_done),
}

@Composable
fun ReadingStatus.getStatusColor(): Color = when (this) {
    ReadingStatus.PLANNED -> ApplicationTheme.colors.readingStatusesColor.plannedStatusColor
    ReadingStatus.READING -> ApplicationTheme.colors.readingStatusesColor.readingStatusColor
    ReadingStatus.DONE -> ApplicationTheme.colors.readingStatusesColor.doneStatusColor
    ReadingStatus.DEFERRED -> ApplicationTheme.colors.readingStatusesColor.deferredStatusColor
}
