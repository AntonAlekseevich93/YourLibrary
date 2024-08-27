package date

import ApplicationTheme
import BaseEvent
import BaseEventScope
import DateUtils
import Strings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component_colors.getDatePickerAppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import main_models.DatePickerType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.date_picker_end_date_title
import yourlibrary.common.resources.generated.resources.date_picker_finish_date_error
import yourlibrary.common.resources.generated.resources.date_picker_start_date_title
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEventScope<BaseEvent>.CommonDatePicker(
    title: String = Strings.date_utils_title,
    state: DatePickerState,
    minimumDate: Long? = null,
    minimumDateErrorRes: StringResource = Res.string.date_picker_finish_date_error,
    datePickerType: DatePickerType,
    onDismissRequest: () -> Unit,
) {
    var showError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val text = mutableStateOf(
        if (state.selectedDateMillis != null) {
            DateUtils.getDateInStringFromMillis(
                state.selectedDateMillis!!, Locale.ROOT
            )
        } else {
            when (datePickerType) {
                DatePickerType.StartDate -> stringResource(Res.string.date_picker_start_date_title)
                DatePickerType.EndDate -> stringResource(Res.string.date_picker_end_date_title)
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = {
            onDismissRequest.invoke()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (state.selectedDateMillis != null) {
                        if (minimumDate != null && state.selectedDateMillis!! < minimumDate) {
                            showError = true
                        } else {
                            this@CommonDatePicker.sendEvent(
                                DatePickerEvents.OnSelectedDate(
                                    state.selectedDateMillis ?: 0, text.value
                                )
                            )
                            onDismissRequest.invoke()
                        }

                    }
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = Strings.select,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            }
        },
        colors = getDatePickerAppColor()
    ) {
        DatePicker(
            state = state,
            colors = getDatePickerAppColor(),
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )
            },
            headline = {
                Text(text.value, modifier = Modifier.padding(start = 16.dp))
            }
        )

        if (showError) {
            scope.launch {
                delay(3000)
                showError = false
            }
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(
                    stringResource(minimumDateErrorRes),
                    color = ApplicationTheme.colors.errorColor
                )
            }
        }
    }
}