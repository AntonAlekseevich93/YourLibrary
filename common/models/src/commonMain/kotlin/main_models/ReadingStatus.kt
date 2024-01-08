package main_models

import Strings

enum class ReadingStatus(val nameValue: String) {
    DEFERRED(Strings.reading_status_deferred),
    PLANNED(Strings.reading_status_planned),
    READING(Strings.reading_status_is_reading),
    DONE(Strings.reading_status_done),
}