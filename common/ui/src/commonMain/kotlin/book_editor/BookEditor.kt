package book_editor

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import book_editor.elements.AuthorIsNotSelectedInfo
import book_editor.elements.NewAuthorButton
import book_editor.elements.authors_selector.AuthorsListSelector
import book_editor.elements.book_selector.BookSearchSelector
import containters.CenterBoxContainer
import date.DatePickerEvents
import images.BookCoverFailureImage
import images.BookCoverLoadingProcessImage
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.AuthorVo
import main_models.BookValues
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import main_models.genre.GenreUtils
import platform.Platform
import platform.isMobile
import reading_status.getStatusColor
import tags.CustomTag
import text_fields.SearchTextField
import text_fields.TextFieldWithTitleAndSuggestion
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_authors
import yourlibrary.common.resources.generated.resources.ic_book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseEventScope<BaseEvent>.BookEditor(
    platform: Platform,
    bookValues: BookValues,
    similarSearchAuthors: List<AuthorVo>,
    selectedAuthor: AuthorVo?,
    statusBookTextFieldValue: MutableState<TextFieldValue>,
    isKeyboardShown: Boolean,
    createNewAuthor: Boolean,
    isSearchBookProcess: Boolean,
    isSearchAuthorProcess: Boolean,
    modifier: Modifier = Modifier,
    canShowError: Boolean = false,
    showSearchBookError: Boolean,
    showSearchAuthorError: Boolean,
    isCreateBookManually: Boolean = true,
    shortBook: BookShortVo? = null,
    isBookCoverManually: Boolean = false,
    bookWasNotFound: Boolean = false,
    authorWasNotFound: Boolean = false,
    similarBooks: List<BookShortVo> = listOf(),
    onClickSave: (() -> Unit)? = null,
    genreSelectorListener: () -> Unit,
) {
    val painter = asyncPainterResource(
        data = bookValues.coverUrl.value.text,
        key = bookValues.coverUrl.value.text
    )

    val authorIsSelected by remember(key1 = selectedAuthor) { mutableStateOf(selectedAuthor != null) }
    var lastSearchBookName by remember { mutableStateOf("") }
    val genre = remember(key1 = bookValues.genre.value, key2 = shortBook) {
        if (shortBook != null) {
            mutableStateOf(GenreUtils.getGenreById(shortBook.bookGenreId).name)
        } else if (bookValues.genre.value != null) {
            mutableStateOf(bookValues.genre.value!!.name)
        } else {
            mutableStateOf("")
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val authorsScrollableState = rememberScrollState()
    val authorFieldIsFocused = remember { mutableStateOf(false) }
    var oldAuthorName by remember { mutableStateOf("") }
    val needSearchAuthor = remember { mutableStateOf(false) }

    if (needSearchAuthor.value && !authorFieldIsFocused.value) {
        val authorTextField = bookValues.authorName.value

        this@BookEditor.sendEvent(
            BookEditorEvents.OnAuthorTextChanged(
                textFieldValue = authorTextField,
                textWasChanged = oldAuthorName != authorTextField.text,
                needNewSearch = true
            )
        )
        oldAuthorName = authorTextField.text
        needSearchAuthor.value = false
    }

    Column(modifier = modifier) {
        AnimatedVisibility(
            isCreateBookManually || shortBook != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val imageModifier = Modifier
                    .sizeIn(
                        minHeight = 200.dp,
                        minWidth = 130.dp,
                        maxHeight = 200.dp,
                        maxWidth = 130.dp
                    )
                Card(
                    modifier = imageModifier,
                    colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box {
                        KamelImage(
                            resource = painter,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            onFailure = {
                                //todo
                            },
                            onLoading = {
                                //todo
                            },
                        )
                        when (painter) {
                            is Resource.Loading -> {
                                BookCoverLoadingProcessImage(
                                    modifier = imageModifier,
                                    randomCover = false
                                )
                            }

                            is Resource.Success -> {
                            }

                            is Resource.Failure -> {
                                BookCoverFailureImage(modifier = imageModifier)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

        if (
            (bookValues.isRequiredFieldsFilled() && createNewAuthor || bookValues.isRequiredFieldsFilled() && selectedAuthor != null) ||
            bookValues.isRequiredFieldsFilled() && shortBook != null
        ) {
            CenterBoxContainer {
                CustomTag(
                    text = Strings.save,
                    color = ApplicationTheme.colors.mainAddButtonColor,
                    textStyle = ApplicationTheme.typography.footnoteBold,
                    textModifier = Modifier,
                    maxHeight = 50.dp,
                    onClick = {
                        onClickSave?.invoke()
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = !isCreateBookManually && shortBook == null,
                ) {
                    Column {
                        SearchTextField(
                            hintText = if (selectedAuthor != null)
                                "${Strings.search_in_author_books} ${selectedAuthor.name}"
                            else Strings.search_by_book_name,
                            textFieldValue = bookValues.bookName,
                            onTextChanged = {
                                val oldText = bookValues.bookName.value.text
                                if (it.text.trim().isEmpty()) {
                                    sendEvent(BookEditorEvents.ClearBookSearch)
                                } else if (authorIsSelected) {
                                    sendEvent(BookEditorEvents.OnBookNameChanged(it.text))
                                } else if (showSearchBookError && oldText != it.text) {
                                    sendEvent(BookEditorEvents.HideSearchError)
                                }

                                bookValues.bookName.value = it
                            },
                            onClickSearch = {
                                keyboardController?.hide()
                                val newSearch = bookValues.bookName.value.text
                                if (lastSearchBookName != newSearch) {
                                    lastSearchBookName = newSearch
                                    sendEvent(BookEditorEvents.OnBookNameChanged(newSearch))
                                }
                            },
                            iconResName = Res.drawable.ic_book
                        )

                        SearchTextField(
                            hintText = Strings.search_by_author,
                            textFieldValue = bookValues.authorName,
                            onTextChanged = {
                                val oldText = bookValues.authorName.value.text
                                bookValues.authorName.value = it

                                this@BookEditor.sendEvent(
                                    BookEditorEvents.OnAuthorTextChanged(
                                        textFieldValue = it,
                                        textWasChanged = oldText != it.text
                                    )
                                )
                            },
                            onClickSearch = {
                                keyboardController?.hide()
                                sendEvent(BookEditorEvents.OnSearchAuthorClick(bookValues.authorName.value.text))
                            },
                            iconResName = Res.drawable.ic_authors
                        )
                    }
                }

                if (!isCreateBookManually) {
                    AuthorsListSelector(
                        isSearchAuthorProcess = isSearchAuthorProcess,
                        showError = showSearchAuthorError,
                        similarSearchAuthors = similarSearchAuthors,
                        bookValues = bookValues
                    )
                }

                if (shortBook == null && !isCreateBookManually) {
                    BookSearchSelector(
                        similarBooks = similarBooks,
                        isLoading = isSearchBookProcess,
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, start = 8.dp),
                        showError = showSearchBookError,
                        bookValues = bookValues,
                        onClick = {
                            sendEvent(BookEditorEvents.OnBookSelected(it))
                        },
                        onClickManually = {
                            sendEvent(BookEditorEvents.OnCreateBookManually(bookWasNotFound = true))
                        },
                        bookHaveReadingStatusEvent = {
                            sendEvent(BookEditorEvents.BookHaveReadingStatusEvent(Strings.bookExistInLibrary))
                        }
                    )
                }

                if (isCreateBookManually || shortBook != null) {
                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.title,
                        hintText = Strings.hint_type_title_book,
                        onTextChanged = {
                            bookValues.bookName.value = it
                        },
                        disableSingleLineIfFocused = true,
                        enabledInput = shortBook == null && !bookWasNotFound,
                        disableBorder = shortBook == null,
                        textFieldValue = bookValues.bookName,
                        maxLines = 1,
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        modifier = Modifier.padding(top = 2.dp),
                        platform = platform,
                        title = Strings.autor,
                        hintText = Strings.hint_type_author,
                        textFieldValue = bookValues.authorName,
                        maxLines = 1,
                        enabledInput = shortBook == null && !authorWasNotFound,
                        disableBorder = shortBook != null,
                        onTextChanged = {
                            bookValues.authorName.value = it
                            needSearchAuthor.value = true
                        },
                        setAsSelected = !authorIsSelected && similarSearchAuthors.isNotEmpty() && !createNewAuthor,
                        innerContent = {
                            Row(
                                modifier = Modifier.fillMaxSize().padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = bookValues.authorName.value.text,
                                    style = ApplicationTheme.typography.bodyRegular,
                                    color = Color.Transparent
                                )
                            }
                        },
                        customIsFocused = authorFieldIsFocused,
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    if (isCreateBookManually && !createNewAuthor && similarSearchAuthors.isNotEmpty()) {
                        keyboardController?.hide()
                        BasicAlertDialog(
                            onDismissRequest = {}
                        ) {
                            Column(Modifier.verticalScroll(authorsScrollableState)) {
                                AuthorIsNotSelectedInfo(modifier = Modifier.padding(top = 8.dp))

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    NewAuthorButton(
                                        createNewAuthor = false,
                                        modifier = Modifier
                                    ) {
                                        this@BookEditor.sendEvent(
                                            BookEditorEvents.OnChangeNeedCreateNewAuthor(true)
                                        )
                                    }
                                }

                                AuthorsListSelector(
                                    isSearchAuthorProcess = isSearchAuthorProcess,
                                    showError = false,
                                    similarSearchAuthors = similarSearchAuthors,
                                    bookValues = bookValues,
                                )
                            }
                        }
                    }

                    if (shortBook == null && bookValues.authorName.value.text.length >= 2 && !authorIsSelected && !authorWasNotFound) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NewAuthorButton(
                                createNewAuthor = createNewAuthor,
                                modifier = Modifier
                            ) {
                                this@BookEditor.sendEvent(
                                    BookEditorEvents.OnChangeNeedCreateNewAuthor(
                                        !createNewAuthor
                                    )
                                )
                            }
                        }
                    }

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.status,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = ReadingStatus.PLANNED.nameValue,
                        textFieldValue = statusBookTextFieldValue,
                        enabledInput = false,
                        onSuggestionClickListener = {
                            bookValues.selectedStatus.value = when (it) {
                                ReadingStatus.PLANNED.nameValue -> {
                                    ReadingStatus.PLANNED
                                }

                                ReadingStatus.READING.nameValue -> {
                                    ReadingStatus.READING
                                }

                                ReadingStatus.DONE.nameValue -> {
                                    ReadingStatus.DONE
                                }

                                ReadingStatus.DEFERRED.nameValue -> {
                                    ReadingStatus.DEFERRED
                                }

                                else -> throw Exception("The type does not match")
                            }
                            statusBookTextFieldValue.value =
                                statusBookTextFieldValue.value.copy(
                                    text = it,
                                    selection = TextRange(it.length)
                                )
                        },
                        showSuggestionAsTag = true,
                        freezeFocusWhenOnClick = true,
                        suggestionList = mutableStateOf(ReadingStatus.entries.map { it.nameValue }),
                        tagColor = bookValues.selectedStatus.value.getStatusColor(),
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.pages_title,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_number_of_pages,
                        enabledInput = shortBook == null,
                        disableBorder = shortBook != null,
                        onTextChanged = {
                            bookValues.numberOfPages.value = it
                        },
                        textFieldValue = bookValues.numberOfPages,
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.genre,
                        modifier = Modifier.padding(top = 2.dp),
                        text = genre,
                        hintText = Strings.genre_selector_hint_text,
                        enabledInput = false,
                        onClick = {
                            if (shortBook == null) {
                                genreSelectorListener()
                            }
                        },
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.cover,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_link_to_cover,
                        enabledInput = shortBook == null || isBookCoverManually,
                        hiddenText = if (shortBook == null || isBookCoverManually) null else Strings.cover_book_manually_title,
                        onTextChanged = {
                            bookValues.coverUrl.value = it
                        },
                        disableSingleLineIfFocused = true,
                        textFieldValue = bookValues.coverUrl,
                        onClick = {
                            if (shortBook != null && !isBookCoverManually) {
                                sendEvent(
                                    BookEditorEvents.OnShowAlertDialogDeleteBookCover(
                                        CommonAlertDialogConfig(
                                            title = Strings.alert_dialog_delete_cover_title,
                                            description = Strings.alert_dialog_delete_cover_description,
                                            acceptButtonTitle = Strings.delete,
                                            dismissButtonTitle = Strings.non_delete,
                                        )
                                    )
                                )
                            }
                        },
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.start_date_title,
                        text = bookValues.startDateInString,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_reading_start_date,
                        enabledInput = false,
                        onClick = {
                            this@BookEditor.sendEvent(
                                DatePickerEvents.OnShowDatePicker(
                                    DatePickerType.StartDate
                                )
                            )
                        },
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        text = bookValues.endDateInString,
                        title = Strings.end_date_title,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_reading_end_date,
                        enabledInput = false,
                        onClick = {
                            this@BookEditor.sendEvent(
                                DatePickerEvents.OnShowDatePicker(
                                    DatePickerType.EndDate
                                )
                            )
                        },
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.description,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_book_description,
                        enabledInput = shortBook == null,
                        disableBorder = shortBook != null,
                        onTextChanged = {
                            bookValues.description.value = it
                        },
                        disableSingleLineIfFocused = true,
                        textFieldValue = bookValues.description,
                        maxLines = 200,
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.isbn,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_isbn_number,
                        enabledInput = shortBook == null,
                        disableBorder = shortBook != null,
                        onTextChanged = {
                            bookValues.isbn.value = it
                        },
                        textFieldValue = bookValues.isbn,
                        titleColor = ApplicationTheme.colors.titleColors.booksTitleInfoColor,
                    )
                }

                /**this use for add padding if keyboard shown**/
                if (platform.isMobile()) {
                    if (isKeyboardShown) {
                        Spacer(Modifier.padding(160.dp))
                    } else {
                        Spacer(Modifier.padding(50.dp))
                    }
                } else {
                    Spacer(Modifier.padding(30.dp))
                }
            }
        }
    }
}





