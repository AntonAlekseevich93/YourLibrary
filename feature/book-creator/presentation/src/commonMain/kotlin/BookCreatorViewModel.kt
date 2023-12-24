import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.BookCreatorUiState

class BookCreatorViewModel(private val repository: BookCreatorRepository) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private val _uiState: MutableStateFlow<BookCreatorUiState> = MutableStateFlow(BookCreatorUiState())
    val uiState = _uiState.asStateFlow()

    fun getBookItem(id: Int){
        //todo получаем книгу из бд

    }

}