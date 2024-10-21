package date

import ApplicationTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import main_models.DatePickerType
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.cancel
import yourlibrary.common.resources.generated.resources.date_picker_delete_end_date_button_title
import yourlibrary.common.resources.generated.resources.date_picker_delete_start_date_and_end_date_button_title
import yourlibrary.common.resources.generated.resources.date_picker_delete_start_date_button_title
import yourlibrary.common.resources.generated.resources.date_selector_menu_change_end_date
import yourlibrary.common.resources.generated.resources.date_selector_menu_change_start_date
import yourlibrary.common.resources.generated.resources.date_selector_menu_delete_confirm_title
import yourlibrary.common.resources.generated.resources.date_selector_menu_end_date
import yourlibrary.common.resources.generated.resources.date_selector_menu_start_date
import yourlibrary.common.resources.generated.resources.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateChangeSelectorDialog(
    startDateSelected: Boolean,
    endDateSelected: Boolean,
    onDeleteDateListener: (dateType: DatePickerType) -> Unit,
    selectDateTypeListener: (dateType: DatePickerType) -> Unit,
    dismiss: () -> Unit
) {
    val startDateDatePickerSelectorText = if (startDateSelected) {
        stringResource(Res.string.date_selector_menu_change_start_date)
    } else {
        stringResource(Res.string.date_selector_menu_start_date)
    }

    val endDateDatePickerSelectorText = if (endDateSelected) {
        stringResource(Res.string.date_selector_menu_change_end_date)
    } else {
        stringResource(Res.string.date_selector_menu_end_date)
    }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var deletingDateType: DatePickerType? by remember { mutableStateOf(null) }

    BasicAlertDialog(
        onDismissRequest = dismiss,
        modifier = Modifier.clip(
            RoundedCornerShape(12.dp)
        ).background(ApplicationTheme.colors.mainBackgroundWindowDarkColor)
    ) {
        if (showDeleteConfirmDialog) {
            Column(Modifier.padding(vertical = 28.dp)) {
                Text(
                    text = stringResource(Res.string.date_selector_menu_delete_confirm_title),
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
                Spacer(Modifier.padding(36.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(Res.string.delete),
                        style = ApplicationTheme.typography.headlineBold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.padding(end = 24.dp).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            deletingDateType?.let {
                                onDeleteDateListener(it)
                                dismiss()
                            }
                        }
                    )
                    Text(
                        text = stringResource(Res.string.cancel),
                        style = ApplicationTheme.typography.headlineBold,
                        color = ApplicationTheme.colors.mainTextColor,
                        modifier = Modifier.padding(end = 16.dp).clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            dismiss()
                        }
                    )
                }
            }
        } else {
            Column(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = startDateDatePickerSelectorText,
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            selectDateTypeListener(DatePickerType.StartDate)
                        }.padding(horizontal = 16.dp, vertical = 16.dp)
                )

                Spacer(Modifier.padding(4.dp))

                Text(
                    text = endDateDatePickerSelectorText,
                    style = ApplicationTheme.typography.headlineMedium,
                    color = ApplicationTheme.colors.mainTextColor,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            selectDateTypeListener(DatePickerType.EndDate)
                        }.padding(horizontal = 16.dp, vertical = 16.dp)
                )

                if (startDateSelected) {
                    val menuText = if (endDateSelected) {
                        stringResource(Res.string.date_picker_delete_start_date_and_end_date_button_title)
                    } else {
                        stringResource(Res.string.date_picker_delete_start_date_button_title)
                    }
                    Text(
                        text = menuText,
                        style = ApplicationTheme.typography.headlineMedium,
                        color = ApplicationTheme.colors.mainTextColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = ripple()
                            ) {
                                showDeleteConfirmDialog = true
                                deletingDateType = DatePickerType.StartDate
                            }.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                if (endDateSelected) {
                    Text(
                        text = stringResource(Res.string.date_picker_delete_end_date_button_title),
                        style = ApplicationTheme.typography.headlineMedium,
                        color = ApplicationTheme.colors.mainTextColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = ripple()
                            ) {
                                showDeleteConfirmDialog = true
                                deletingDateType = DatePickerType.EndDate
                            }.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}