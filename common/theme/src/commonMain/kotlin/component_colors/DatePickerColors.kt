package component_colors

import ApplicationTheme
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class DatePickerColors(
    val containerColor: Color,
    val textColor: Color,
    val selectedTextColor: Color,
    val selectedContainerColor: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getDatePickerAppColor(): DatePickerColors {
    ApplicationTheme.colors.datePickerColors.apply {
        return DatePickerDefaults.colors(
            containerColor = containerColor,
            titleContentColor = textColor,
            headlineContentColor = textColor,
            weekdayContentColor = textColor,
            subheadContentColor = textColor,
            yearContentColor = textColor,
            currentYearContentColor = textColor,
            selectedYearContentColor = textColor,
            selectedYearContainerColor = selectedContainerColor,
            dayContentColor = textColor,
            disabledDayContentColor = textColor,
            selectedDayContentColor = selectedTextColor,
            disabledSelectedDayContentColor = textColor,
            selectedDayContainerColor = selectedContainerColor.copy(alpha = 0.5f),
            disabledSelectedDayContainerColor = selectedContainerColor,
            todayContentColor = selectedContainerColor,
            todayDateBorderColor = selectedContainerColor,
            dayInSelectionRangeContentColor = textColor,
            dayInSelectionRangeContainerColor = textColor,
        )
    }
}