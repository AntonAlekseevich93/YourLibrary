import androidx.compose.ui.text.TextRange
import book_editor.BookEditorEvents
import date.DatePickerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import main_models.AuthorVo
import main_models.DatePickerType
import models.BookInfoScope
import models.BookInfoUiState
import models.BookScreenEvents
import navigation_drawer.contents.models.DrawerEvents
import platform.PlatformInfoData
import toolbar.ToolbarEvents
import tooltip_area.TooltipEvents

class BookInfoViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BookInfoInteractor,
    private val navigationHandler: NavigationHandler,
    private val tooltipHandler: TooltipHandler,
    private val applicationScope: ApplicationScope,
    private val drawerScope: DrawerScope,
) : BookInfoScope<BaseEvent> {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookInfoUiState> = MutableStateFlow(BookInfoUiState())
    val uiState = _uiState.asStateFlow()

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is TooltipEvents.SetTooltipEvent -> tooltipHandler.setTooltip(event.tooltip)
            is DrawerEvents.OpenLeftDrawerOrCloseEvent -> drawerScope.openLeftDrawerOrClose()
            is DrawerEvents.OpenRightDrawerOrCloseEvent -> drawerScope.openRightDrawerOrClose()
            is BookScreenEvents.BookScreenCloseEvent -> applicationScope.closeBookScreen()
            is BookScreenEvents.SaveBookAfterEditing -> {
                //todo
            }
            is BookScreenEvents.SetEditMode -> _uiState.value.isEditMode.value = true
            is BookScreenEvents.ChangeReadingStatusEvent -> {
                //todo
            }

            is BookEditorEvents.OnAuthorTextChanged -> {
                //todo
            }

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)
            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                _uiState.value.showDatePicker.value = false
            }

            is ToolbarEvents.ToMain -> navigationHandler.navigateToMain()
        }
    }



    private fun clearSearchAuthor() {
        _uiState.value.clearSimilarAuthorList()
    }









    private fun setSelectedDate(millis: Long, text: String) {
        _uiState.value.apply {
            when (datePickerType.value) {
                DatePickerType.StartDate -> {
                    bookValues.value.startDateInMillis.value = millis
                    bookValues.value.startDateInString.value = text
                }

                DatePickerType.EndDate -> {
                    bookValues.value.endDateInMillis.value = millis
                    bookValues.value.endDateInString.value = text
                }
            }
        }
    }

    private fun showDatePicker(type: DatePickerType) {
        _uiState.value.apply {
            datePickerType.value = type
            showDatePicker.value = true
        }
    }

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        _uiState.value.apply {
            setSelectedAuthor(author)
            bookValues.value.authorName.value = bookValues.value.authorName.value.copy(
                author.name,
                selection = TextRange(author.name.length)
            )
            clearSearchAuthor()
        }
    }
}