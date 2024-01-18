package main_models

import Strings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ReadingStatusVo(
    val status: ReadingStatus,
    val booksList: MutableList<BookItemVo> = mutableListOf()
) {
    companion object {
        fun createShelvesListFromStatuses(): List<ShelfVo> = ReadingStatus.entries.map { status ->
            ShelfVo(
                id = status.id,
                name = status.nameValue,
                booksList = mutableListOf()
            )
        }
    }
}

enum class ReadingStatus(val id: String, val nameValue: String) {
    PLANNED(id = "id_planned", nameValue = Strings.reading_status_planned),
    READING(id = "id_reading", nameValue = Strings.reading_status_is_reading),
    DONE(id = "id_done", nameValue = Strings.reading_status_done),
    DEFERRED(id = "id_deferred", nameValue = Strings.reading_status_deferred),
}

@Composable
fun ReadingStatus.getStatusColor(): Color = when (this) {
    ReadingStatus.PLANNED -> ApplicationTheme.colors.readingStatusesColor.plannedStatusColor
    ReadingStatus.READING -> ApplicationTheme.colors.readingStatusesColor.readingStatusColor
    ReadingStatus.DONE -> ApplicationTheme.colors.readingStatusesColor.doneStatusColor
    ReadingStatus.DEFERRED -> ApplicationTheme.colors.readingStatusesColor.deferredStatusColor
}
