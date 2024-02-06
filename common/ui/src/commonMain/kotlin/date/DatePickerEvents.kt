package date

import BaseEvent
import main_models.DatePickerType

sealed class DatePickerEvents : BaseEvent {
    class OnSelectedDate(val millis: Long, val text: String) : DatePickerEvents()
    class OnShowDatePicker(val type: DatePickerType) : DatePickerEvents()
    data object OnHideDatePicker : DatePickerEvents()
}