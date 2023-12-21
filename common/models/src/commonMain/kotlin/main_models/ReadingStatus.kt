package main_models

enum class ReadingStatus(val nameValue: String) {
    PLANNED(Strings.reading_status_planned),
    READING(Strings.reading_status_is_reading),
    DONE(Strings.reading_status_done)
}