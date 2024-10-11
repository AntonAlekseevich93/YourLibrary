import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.input.TextFieldValue
import base.BaseMVIViewModel
import book_editor.elements.BookEditorEvents
import date.DatePickerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main_models.AuthorVo
import main_models.BookVo
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import models.BookCreatorEvents
import models.BookCreatorUiState
import models.SelectedBook
import platform.PlatformInfoData
import text_fields.DELAY_FOR_LISTENER_PROCESSING

class BookCreatorViewModel(
    private val platformInfo: PlatformInfoData,
    private val interactor: BookCreatorInteractor,
    private val appConfig: AppConfig,
) : BaseMVIViewModel<BookCreatorUiState, BaseEvent>(BookCreatorUiState()) {
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())
    private var searchJob: Job? = null
    private var userBookSearchJob: Job? = null

    init {
        uiState.value.isHazeBlurEnabled.value = platformInfo.isHazeBlurEnabled
    }

    override fun sendEvent(event: BaseEvent) {
        when (event) {
            is BookEditorEvents.OnAuthorTextChanged -> onAuthorTextChanged(
                event.textFieldValue,
                event.textWasChanged,
                event.needNewSearch
            )

            is BookEditorEvents.OnBookNameChanged -> {
                searchBookName(event.bookName)
            }

            is BookEditorEvents.OnSuggestionAuthorClickEvent -> onSuggestionAuthorClick(event.author)

            is BookEditorEvents.OnSearchAuthorClick -> {
                searchAuthor(event.name)
            }

            is BookEditorEvents.ClearBookSearch -> {
                if (uiStateValue.selectedAuthor == null) {
                    updateSimilarBooks(emptyList())
                    updateSimilarBooksCache(emptyList())
                } else {
                    updateSimilarBooks(emptyList())
                    uiStateValue.similarBooks = uiStateValue.similarBooksCache.toMutableStateList()
                }
                updateUIState(uiStateValue.copy(showSearchBookError = false))
            }

            is BookEditorEvents.HideSearchError -> {
                hideSearchError()
            }

            is BookCreatorEvents.ClearUrlEvent -> clearUrl()

            is BookCreatorEvents.ClearAuthorSearch -> {
                uiStateValue.bookValues.clearAuthor()
                updateUIState(uiStateValue.copy(similarSearchAuthors = emptyList()))
            }

            is BookCreatorEvents.ClearBooksSearch -> {
                uiStateValue.bookValues.clearBook()
                updateSimilarBooks(emptyList())
                updateSimilarBooksCache(emptyList())
            }

            is BookCreatorEvents.OnShowDialogClearAllData -> {
                updateUIState(uiStateValue.copy(showDialogClearAllData = event.show))
            }

            is BookCreatorEvents.SetSelectedBookByMenuClick -> {
                setSelectedBookByMenuClick(event.bookId)
            }

            is BookCreatorEvents.ClearSelectedBook -> {
                uiStateValue.selectedBookByMenuClick.value = null
            }

            is BookCreatorEvents.ChangeBookReadingStatus -> {
                changeBookReadingStatusIfBookExistOrCreateBookWithNewStatus(event.newStatus)
            }

            is BookCreatorEvents.CreateManuallyBook -> {
                createManuallyBook()
            }

            is BookCreatorEvents.StartUserBookSearchAuthor -> {
                userBookCreatorSearchAuthor(event.searchedText)
            }

            is BookCreatorEvents.StartUserBookSearchInAuthorBooks -> {
                searchInCachedAuthorsBooks(event.searchedText)
            }

            is BookCreatorEvents.CancelUserBookSearchAuthor -> {
                cancelUserBookSearchAuthor()
            }

            is BookCreatorEvents.ClearMatchesBooksBySelectedAuthors -> {
                uiStateValue.userBookCreatorUiState.apply {
                    similarSearchedBooksByAuthor.value = emptyList()
                    exactMatchSearchedBooks.value = emptyList()
                }
            }

            is BookCreatorEvents.UserBookCreatorAuthorSelected -> {
                uiStateValue.userBookCreatorUiState.apply {
                    exactMatchSearchedAuthor.value = event.author
                    similarSearchedAuthors.value = emptyList()
                    authorNameTextState.value =
                        TextFieldValue(event.author.name)
                    oldTypedAuthorNameText.value = authorNameTextState.value.text
                    if (bookNameTextState.value.text.isNotEmpty()) {
                        userBookCreatorSearchBookInAuthor(
                            bookName = bookNameTextState.value.text,
                            selectedAuthor = event.author
                        )
                    }
                }
            }

            is BookCreatorEvents.CheckIfAuthorIsMatchingAndSetOnCreatedUserScreen -> {
                checkIfAuthorIsMatchingAndSetOnCreatedUserScreen()
            }

            is DatePickerEvents.OnSelectedDate -> setSelectedDate(event.millis, event.text)
            is DatePickerEvents.OnShowDatePicker -> showDatePicker(event.type)
            is DatePickerEvents.OnHideDatePicker -> {
                updateUIState(
                    uiStateValue.copy(
                        showDatePicker = false
                    )
                )
            }
        }
    }

    private fun cancelUserBookSearchAuthor() {
        userBookSearchJob?.cancel()
        uiStateValue.userBookCreatorUiState.apply {
            exactMatchSearchedAuthor.value = null
            hideUserBookCreatorSearchAuthorLoader()
            similarSearchedAuthors.value = emptyList()
            selectedAuthorBooks.value = emptyList()
            exactMatchSearchedBooks.value = emptyList()
            similarSearchedBooksByAuthor.value = emptyList()
        }
    }

    private suspend fun getOrCreateAuthor(book: BookVo): AuthorVo {
        val localAuthor = interactor.getLocalAuthorById(book.originalAuthorId)
        return localAuthor
            ?: if (uiStateValue.selectedAuthor != null) {
                uiStateValue.selectedAuthor!!
            } else {
                AuthorVo(
                    serverId = null,
                    localId = null,
                    id = book.originalAuthorId,
                    name = book.originalAuthorName,
                    uppercaseName = book.originalAuthorName.uppercase(),
                    timestampOfCreating = 0,
                    timestampOfUpdating = 0,
                    isCreatedByUser = true,
                    firstName = book.authorFirstName,
                    lastName = book.authorLastName,
                    middleName = book.authorMiddleName,
                )
            }
    }

    private fun clearSearchAuthor(showError: Boolean = false) {
        updateUIState(
            state = uiStateValue.copy(
                isSearchAuthorProcess = false,
                showSearchAuthorError = showError,
                similarSearchAuthors = emptyList()
            )
        )
    }

    private fun onAuthorTextChanged(
        textFieldValue: TextFieldValue,
        textWasChanged: Boolean,
        needNewSearch: Boolean
    ) {
        if (textWasChanged) {
            updateSimilarBooks(emptyList())
            updateSimilarBooksCache(emptyList())
        }

        if (textWasChanged && uiStateValue.selectedAuthor != null && uiStateValue.bookValues.isSelectedAuthorNameWasChanged()) {
            updateUIState(uiStateValue.copy(selectedAuthor = null))
        }

        if (textWasChanged && (uiStateValue.showSearchAuthorError || uiStateValue.showSearchBookError)) {
            updateUIState(
                uiStateValue.copy(
                    showSearchAuthorError = false,
                    showSearchBookError = false
                )
            )
        }
        if (textFieldValue.text.isEmpty()) {
            clearSearchAuthor()
        } else if (textWasChanged && needNewSearch) {
            searchJob?.cancel()
            searchJob = scope.launch(Dispatchers.IO) {
                searchAuthor(textFieldValue.text)
            }
        }
    }

    private fun searchAuthor(authorName: String) {
        searchJob?.cancel()
        updateUIState(
            uiStateValue.copy(
                isSearchAuthorProcess = false,
                similarSearchAuthors = emptyList(),
                showSearchBookError = false,
                isSearchBookProcess = false
            )
        )
        val uppercaseName = authorName.trim().uppercase()
        if (authorName.length >= 2) {
            updateUIState(uiStateValue.copy(isSearchAuthorProcess = true))
            searchJob = scope.launch {
                val response = interactor.searchInAuthorsNameWithRelates(uppercaseName)
                if (response.isNotEmpty()) {
                    val list: MutableList<AuthorVo> = mutableListOf()
                    list.addAll(response)
                    val exactMatchAuthor = list.find { it.uppercaseName == uppercaseName }

                    if (exactMatchAuthor == null) {
                        updateUIState(
                            uiStateValue.copy(
                                similarSearchAuthors = list,
                                isSearchAuthorProcess = false
                            )
                        )
                    } else {
                        updateUIState(
                            uiStateValue.copy(
                                similarSearchAuthors = list,
                                selectedAuthor = exactMatchAuthor,
                                isSearchAuthorProcess = false
                            )
                        )
                    }
                } else {
                    clearSearchAuthor(showError = true)
                }
            }
        } else {
            clearSearchAuthor()
        }
    }

    private fun userBookCreatorSearchAuthor(authorName: String) {
        userBookSearchJob?.cancel()
        uiStateValue.userBookCreatorUiState.exactMatchSearchedAuthor.value = null
        hideUserBookCreatorSearchAuthorLoader()
        val uppercaseName = authorName.trim().uppercase()
        if (authorName.length >= 2) {
            uiStateValue.userBookCreatorUiState.showSearchAuthorLoader.value = true
            userBookSearchJob = scope.launch {
                delay(500)
                val response = interactor.searchInAuthorsNameWithRelates(uppercaseName)
                if (response.isNotEmpty()) {
                    val exactMatchAuthor = response.findMatchingAuthor(uppercaseName)
                    if (exactMatchAuthor != null) {
                        uiStateValue.userBookCreatorUiState.exactMatchSearchedAuthor.value =
                            exactMatchAuthor
                        if (uiStateValue.userBookCreatorUiState.bookNameTextState.value.text.isNotEmpty()) {
                            userBookCreatorSearchBookInAuthor(
                                bookName = uiStateValue.userBookCreatorUiState.bookNameTextState.value.text,
                                selectedAuthor = exactMatchAuthor
                            )
                        }
                    } else {
                        uiStateValue.userBookCreatorUiState.similarSearchedAuthors.value = response
                    }
                }
                hideUserBookCreatorSearchAuthorLoader()
            }
        } else {
            hideUserBookCreatorSearchAuthorLoader()
        }
    }

    private fun hideUserBookCreatorSearchAuthorLoader() {
        uiStateValue.userBookCreatorUiState.showSearchAuthorLoader.value = false
    }

    private fun hideUserBookCreatorSearchBooksLoader() {
        uiStateValue.userBookCreatorUiState.showSearchBooksLoader.value = false
    }

    private fun searchBookName(bookName: String) {
        searchJob?.cancel()
        updateUIState(
            uiStateValue.copy(
                isSearchBookProcess = false,
                showSearchBookError = false,
                isSearchAuthorProcess = false
            )
        )
        if (uiStateValue.selectedAuthor != null) {
            findInSimilarBooks(bookName)
        } else {
            val uppercaseBookName = bookName.trim().uppercase()
            updateSimilarBooks(emptyList())
            updateSimilarBooksCache(emptyList())
            if (bookName.length >= 2) {
                updateUIState(uiStateValue.copy(isSearchBookProcess = true))
                searchJob = scope.launch(Dispatchers.IO) {
                    delay(500)
                    val response = interactor.searchInBooks(uppercaseBookName)
                    val newList = mutableStateListOf<BookShortVo>()

                    newList.addAll(response)
                    newList.map {
                        val status: ReadingStatus? = interactor.getBookStatusByBookId(it.bookId)
                        it.copy(localReadingStatus = status)
                    }

                    withContext(Dispatchers.Main) {
                        updateUIState(
                            uiStateValue.copy(
                                similarBooks = newList,
                                similarBooksCache = newList,
                                isSearchBookProcess = false,
                                showSearchBookError = newList.isEmpty()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun userBookCreatorSearchBookInAuthor(bookName: String, selectedAuthor: AuthorVo) {
        userBookSearchJob?.cancel()
        uiStateValue.userBookCreatorUiState.exactMatchSearchedBooks.value = emptyList()
        hideUserBookCreatorSearchBooksLoader()
        val uppercaseBookName = bookName.trim().uppercase()
        if (bookName.length >= 2) {
            uiStateValue.userBookCreatorUiState.showSearchBooksLoader.value = true
            userBookSearchJob = scope.launch {
                delay(500)
                val response = interactor.getAllBooksByAuthor(selectedAuthor.id)
                if (response.isNotEmpty()) {
                    uiStateValue.userBookCreatorUiState.selectedAuthorBooks.value =
                        response
                    val exactMatch = response.findExactMatchingBooks(uppercaseBookName)
                    uiStateValue.userBookCreatorUiState.exactMatchSearchedBooks.value = exactMatch

                    uiStateValue.userBookCreatorUiState.similarSearchedBooksByAuthor.value =
                        response.filterBooksByQuery(uppercaseBookName)
                            .filterNot { it in exactMatch }

                }
                hideUserBookCreatorSearchBooksLoader()
            }
        } else {
            hideUserBookCreatorSearchBooksLoader()
        }
    }

    private fun searchInCachedAuthorsBooks(searchedText: String) {
        userBookSearchJob?.cancel()
        uiStateValue.userBookCreatorUiState.exactMatchSearchedBooks.value = emptyList()
        hideUserBookCreatorSearchBooksLoader()
        val uppercaseBookName = searchedText.trim().uppercase()
        if (uppercaseBookName.length >= 2) {
            uiStateValue.userBookCreatorUiState.showSearchBooksLoader.value = true
            userBookSearchJob = scope.launch {
                val allBooks = uiStateValue.userBookCreatorUiState.selectedAuthorBooks.value
                val exactMatch = allBooks.findExactMatchingBooks(uppercaseBookName)
                uiStateValue.userBookCreatorUiState.exactMatchSearchedBooks.value = exactMatch

                uiStateValue.userBookCreatorUiState.similarSearchedBooksByAuthor.value =
                    allBooks.filterBooksByQuery(uppercaseBookName)
                        .filterNot { it in exactMatch }
                hideUserBookCreatorSearchBooksLoader()
            }
        } else {
            hideUserBookCreatorSearchBooksLoader()
        }
    }

    private fun findInSimilarBooks(bookName: String) {
        val result = getAllBooksFromCacheWhereNameIs(bookName)
        updateUIState(uiStateValue.copy(showSearchBookError = result.isEmpty()))
        updateSimilarBooks(result)
    }

    private fun getAllBooksFromCacheWhereNameIs(name: String): List<BookShortVo> {
        val newList = uiStateValue.similarBooksCache.filter {
            it.bookName.contains(
                name,
                ignoreCase = true
            )
        }
        return newList
    }

    private fun onSuggestionAuthorClick(author: AuthorVo) {
        updateUIState(uiStateValue.copy(selectedAuthor = author))
        scope.launch {
            delay(DELAY_FOR_LISTENER_PROCESSING)
            clearSearchAuthor()
            getAllBooksByAuthor(author)
        }
    }

    private fun getAllBooksByAuthor(author: AuthorVo) {
        updateUIState(uiStateValue.copy(isSearchBookProcess = false, showSearchBookError = false))
        searchJob?.cancel()
        searchJob = scope.launch(Dispatchers.IO) {
            updateUIState(uiStateValue.copy(isSearchBookProcess = true))
            val response = interactor.getAllBooksByAuthor(author.id)
            updateSimilarBooksCache(response)
            val bookName = uiStateValue.bookValues.bookName.value.text
            var showSearchBookError = response.isEmpty()
            if (bookName.isNotEmpty()) {
                val similarBooks = getAllBooksFromCacheWhereNameIs(bookName).toMutableStateList()
                uiStateValue.similarBooks = similarBooks
                showSearchBookError = similarBooks.isEmpty()
            } else {
                uiStateValue.similarBooks = response.toMutableStateList()
            }

            withContext(Dispatchers.Main) {
                updateUIState(
                    uiStateValue.copy(
                        isSearchBookProcess = false,
                        showSearchBookError = showSearchBookError
                    )
                )
            }
        }
    }

    private fun updateSimilarBooksCache(list: List<BookShortVo>) {
        updateUIState(
            uiStateValue.copy(similarBooksCache = list)
        )
    }

    private fun updateSimilarBooks(list: List<BookShortVo>) {
        updateUIState(
            uiStateValue.copy(similarBooks = list)
        )
    }

    private fun clearUrl() {
        uiStateValue.bookValues.clearAll()
        updateUIState(
            uiStateValue.copy(
                showDialogClearAllData = false,
            )
        )
    }

    private fun setSelectedDate(millis: Long, text: String) {
        uiStateValue.apply {
            when (datePickerType.value) {
                DatePickerType.StartDate -> {
                    uiStateValue.userBookCreatorUiState.startDate.value = millis
                }

                DatePickerType.EndDate -> {
                    uiStateValue.userBookCreatorUiState.endDate.value = millis
                }
            }
        }
    }

    private fun showDatePicker(type: DatePickerType) {
        uiStateValue.datePickerType.value = type
        updateUIState(
            uiStateValue.copy(
                showDatePicker = true
            )
        )
    }

    private fun hideSearchError() {
        updateUIState(uiStateValue.copy(showSearchBookError = false))
    }

    private fun changeBookReadingStatusIfBookExistOrCreateBookWithNewStatus(newStatus: ReadingStatus) {
        scope.launch(Dispatchers.IO) {
            uiStateValue.selectedBookByMenuClick.value?.let { selectedBookInfo ->
                uiStateValue.selectedBookByMenuClick.value = null
                val shortBook =
                    uiStateValue.similarBooks.find { it.bookId == selectedBookInfo.bookId }
                val shortBookIndex =
                    uiStateValue.similarBooks.indexOf(shortBook).takeIf { it >= 0 }
                val shortBookWithNewStatus = shortBook?.copy(
                    localReadingStatus = newStatus
                )
                if (shortBookIndex != null && shortBookWithNewStatus != null) {
                    val oldBooksList =
                        uiStateValue.similarBooks.map { if (it.bookId == shortBookWithNewStatus.bookId) shortBookWithNewStatus else it }

                    updateUIState(uiStateValue.copy(similarBooks = oldBooksList))

                    if (selectedBookInfo.bookVo?.readingStatus?.id == null) {
                        val bookVo = createUserBookBasedOnShortBook(
                            shortBookWithNewStatus
                        )
                        val authorVo = getOrCreateAuthor(bookVo)
                        interactor.createBook(
                            bookVo,
                            author = authorVo,
                        )
                    } else if (selectedBookInfo.bookVo.readingStatus.id != newStatus.id) {
                        interactor.changeUserBookReadingStatus(
                            book = selectedBookInfo.bookVo,
                            newStatus = newStatus
                        )
                    }
                }
            }
        }
    }

    private fun setSelectedBookByMenuClick(bookId: String) {
        scope.launch(Dispatchers.IO) {
            val localBook = interactor.getLocalBookById(bookId)
            withContext(Dispatchers.Main) {
                uiStateValue.selectedBookByMenuClick.value = SelectedBook(
                    bookId = bookId,
                    bookVo = localBook,
                )
            }
        }
    }

    private fun createManuallyBook() {
        uiStateValue.userBookCreatorUiState.createUserBook(
            selectedAuthor = uiStateValue.selectedAuthor,
            userId = appConfig.userId,
            isServiceDevelopmentBook = uiStateValue.isServiceDevelopment.value
        )?.let { book ->
            val author =
                if (uiStateValue.selectedAuthor != null) uiStateValue.selectedAuthor!!
                else if (uiStateValue.userBookCreatorUiState.exactMatchSearchedAuthor.value != null) {
                    uiStateValue.userBookCreatorUiState.exactMatchSearchedAuthor.value!!
                } else {
                    AuthorVo(
                        serverId = null,
                        localId = null,
                        id = book.originalAuthorId,
                        name = book.originalAuthorName,
                        uppercaseName = book.originalAuthorName.uppercase(),
                        timestampOfCreating = 0,
                        timestampOfUpdating = 0,
                        isCreatedByUser = true,
                        firstName = book.authorFirstName,
                        lastName = book.authorLastName,
                        middleName = book.authorMiddleName,
                    )
                }
            scope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    uiStateValue.showLoadingProcessAnimation.value = true
                }
                interactor.createBook(book, author)
                withContext(Dispatchers.Main) {
                    uiStateValue.showCreatedManuallyBookAnimation.value = true
                    uiStateValue.showLoadingProcessAnimation.value = false
                }
                launch {
                    delay(1500)
                    clearAllDataAfterCreatingBook()
                }
            }
        }
    }

    private fun checkIfAuthorIsMatchingAndSetOnCreatedUserScreen() {
        val matchAuthor =
            if (uiStateValue.selectedAuthor != null) uiStateValue.selectedAuthor else {
                uiStateValue.similarSearchAuthors.findMatchingAuthor(uiStateValue.bookValues.authorName.value.text)
            }
        if (matchAuthor != null) {
            uiStateValue.userBookCreatorUiState.exactMatchSearchedAuthor.value = matchAuthor
            val searchedBookName = uiStateValue.bookValues.bookName.value.text
            if (searchedBookName.isNotEmpty()) {
                userBookCreatorSearchBookInAuthor(
                    bookName = searchedBookName,
                    selectedAuthor = matchAuthor
                )
            } else {
                scope.launch {
                    val response = interactor.getAllBooksByAuthor(matchAuthor.id)
                    if (response.isNotEmpty()) {
                        uiStateValue.userBookCreatorUiState.selectedAuthorBooks.value =
                            response
                    }
                }
            }
        }
    }

    private fun List<AuthorVo>.findMatchingAuthor(query: String): AuthorVo? {
        val queryWords = query.uppercase().split(" ").sorted()
        return this.filter { author ->
            val authorWords = author.name.uppercase().split(" ").sorted()
            authorWords == queryWords
        }.firstOrNull()
    }

    private fun List<BookShortVo>.findExactMatchingBooks(query: String): List<BookShortVo> {
        val queryWords = query.uppercase().split(" ").sorted()
        return this.filter { book ->
            val booksNames = book.bookName.uppercase().split(" ").sorted()
            booksNames == queryWords
        }
    }

    private fun List<BookShortVo>.filterBooksByQuery(query: String): List<BookShortVo> {
        val uppercaseQuery = query.trim().uppercase()
        val queryWords = uppercaseQuery.split(" ")
            .map { it.uppercase() }
            .filter { it.length >= 2 }

        return this.filter { book ->
            val bookNameWords = book.bookName.uppercase().split(" ")
            queryWords.all { word -> bookNameWords.any { it.contains(word) } }
        }
    }

    private fun clearAllDataAfterCreatingBook() {
        updateUIState(BookCreatorUiState())
    }

    private fun createUserBookBasedOnShortBook(shortBook: BookShortVo): BookVo =
        BookVo(
            bookId = shortBook.bookId,
            serverId = shortBook.id,
            localId = null,
            originalAuthorId = shortBook.originalAuthorId,
            bookName = shortBook.bookName,
            bookNameUppercase = shortBook.bookName.uppercase(),
            originalAuthorName = shortBook.originalAuthorName,
            description = shortBook.description,
            /**we don`t save covers link. Only image imageName**/
            userCoverUrl = null,
            pageCount = shortBook.numbersOfPages,
            isbn = shortBook.isbn,
            readingStatus = shortBook.localReadingStatus ?: ReadingStatus.PLANNED,
            ageRestrictions = shortBook.ageRestrictions,
            bookGenreId = shortBook.bookGenreId,
            startDateInString = "",
            endDateInString = "",
            startDateInMillis = 0,
            endDateInMillis = 0,
            timestampOfCreating = 0,
            timestampOfUpdating = 0,
            isRussian = shortBook.isRussian,
            imageName = shortBook.imageName,
            authorIsCreatedManually = uiStateValue.selectedAuthor?.isCreatedByUser ?: false,
            isLoadedToServer = false,
            bookIsCreatedManually = false,
            imageFolderId = shortBook.imageFolderId,
            ratingValue = shortBook.ratingValue,
            ratingCount = shortBook.ratingCount,
            reviewCount = shortBook.reviewCount,
            ratingSum = shortBook.ratingSum,
            isServiceDevelopmentBook = false,
            originalMainBookId = shortBook.getMainBookIdByShortBook(),
            lang = shortBook.lang,
            publicationYear = shortBook.publicationYear,
            userId = appConfig.userId,
            authorFirstName = shortBook.authorFirstName,
            authorLastName = shortBook.authorLastName,
            authorMiddleName = shortBook.authorMiddleName,
        )
}