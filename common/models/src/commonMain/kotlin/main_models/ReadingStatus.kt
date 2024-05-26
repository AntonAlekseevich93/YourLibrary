package main_models

import Strings

data class ReadingStatusVo(
    val status: ReadingStatus,
) {
    companion object {
        fun createShelvesListFromStatuses(): List<ShelfVo> = ReadingStatus.entries.map { status ->
            ShelfVo(
                id = status.id,
                name = status.nameValue,
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

object ReadingStatusUtils {
    fun textToReadingStatus(text: String) = when (text) {
        ReadingStatus.PLANNED.nameValue -> ReadingStatus.PLANNED
        ReadingStatus.READING.nameValue -> ReadingStatus.READING
        ReadingStatus.DONE.nameValue -> ReadingStatus.DONE
        ReadingStatus.DEFERRED.nameValue -> ReadingStatus.DEFERRED
        else -> {
            ReadingStatus.PLANNED
        }
    }
}
