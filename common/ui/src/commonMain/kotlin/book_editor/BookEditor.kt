package book_editor

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import book_editor.elements.AuthorIsNotSelectedInfo
import book_editor.elements.LinkToAuthorButton
import book_editor.elements.NewAuthorButton
import book_editor.elements.book_selector.BookSearchSelector
import date.DatePickerEvents
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.AuthorVo
import main_models.BookValues
import main_models.DatePickerType
import main_models.ReadingStatus
import main_models.books.BookShortVo
import platform.Platform
import platform.isMobile
import reading_status.getStatusColor
import text_fields.DELAY_FOR_LISTENER_PROCESSING
import text_fields.DropdownSuggestionItem
import text_fields.TextFieldWithTitleAndSuggestion

@Composable
fun BaseEventScope<BaseEvent>.BookEditor(
    platform: Platform,
    bookValues: BookValues,
    similarSearchAuthors: SnapshotStateList<AuthorVo>,
    selectedAuthor: AuthorVo?,
    statusBookTextFieldValue: MutableState<TextFieldValue>,
    isKeyboardShown: Boolean,
    createNewAuthor: Boolean,
    isSearchBookProcess: Boolean,
    modifier: Modifier = Modifier,
    canShowError: Boolean = false,
    isCreateBookManually: Boolean = true,
    shortBook: BookShortVo? = null,
    similarBooks: SnapshotStateList<BookShortVo> = mutableStateListOf(),
) {
    val showImage = remember { mutableStateOf(false) }
    val painter = asyncPainterResource(
        data = bookValues.coverUrl.value.text,
        key = bookValues.coverUrl.value.text
    )
    when (painter) {
        is Resource.Loading -> {

        }

        is Resource.Success -> {
            showImage.value = true
        }

        is Resource.Failure -> {
            showImage.value = false
        }
    }

    var linkToAuthor by remember { mutableStateOf(false) }

    val authorIsSelected by remember(key1 = selectedAuthor) { mutableStateOf(selectedAuthor != null) }

    Column(modifier = modifier) {
        AnimatedVisibility(
            showImage.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    modifier = Modifier
                        .sizeIn(
                            minHeight = 200.dp,
                            minWidth = 130.dp,
                            maxHeight = 200.dp,
                            maxWidth = 130.dp
                        ),
                    colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
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
                }

                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TextFieldWithTitleAndSuggestion(
                    platform = platform,
                    title = Strings.title,
                    hintText = Strings.hint_type_title_book,
                    onTextChanged = {
                        bookValues.bookName.value = it
                        sendEvent(BookEditorEvents.OnBookNameChanged(it.text))
                    },
                    disableSingleLineIfFocused = true,
                    enabledInput = shortBook == null,
                    disableBorder = shortBook != null,
                    textFieldValue = bookValues.bookName
                )

                TextFieldWithTitleAndSuggestion(
                    modifier = Modifier.padding(top = 2.dp),
                    platform = platform,
                    title = Strings.autor,
                    hintText = Strings.hint_type_author,
                    textFieldValue = bookValues.authorName,
                    enabledInput = shortBook == null,
                    disableBorder = shortBook != null,
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
                    setAsSelected = !authorIsSelected && similarSearchAuthors.isNotEmpty() && !createNewAuthor && !linkToAuthor,
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
                            Text(
                                modifier = Modifier.padding(start = 6.dp, top = 2.dp),
                                text = bookValues.relatedAuthorsNames.value,
                                style = ApplicationTheme.typography.footnoteRegular,
                                color = ApplicationTheme.colors.hintColor
                            )
                        }
                    },
                    bottomContent = {
                        AnimatedVisibility(
                            visible = similarSearchAuthors.isNotEmpty() && selectedAuthor == null && !createNewAuthor && !linkToAuthor,
                            exit = fadeOut(
                                animationSpec = tween(
                                    1,
                                    DELAY_FOR_LISTENER_PROCESSING.toInt()
                                )
                            )
                        ) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = ApplicationTheme.colors.dropdownBackground
                                ),
                                modifier = Modifier.sizeIn(maxHeight = 140.dp)

                            ) {
                                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                                    items(similarSearchAuthors) { author ->
                                        val textPostfix = if (author.relatedAuthors.isNotEmpty()) {
                                            "(${author.relatedAuthors.joinToString { it.name }})"
                                        } else ""
                                        val text = "${author.name}$textPostfix"
                                        DropdownSuggestionItem(
                                            text = text,
                                            itemClickListener = {
                                                bookValues.setSelectedAuthorName(
                                                    author.name,
                                                    relatedAuthorsNames = textPostfix
                                                )
                                                this@BookEditor.sendEvent(
                                                    BookEditorEvents.OnSuggestionAuthorClickEvent(
                                                        author
                                                    )
                                                )
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = (platform.isMobile() || canShowError) && shortBook == null && selectedAuthor == null &&
                                    similarSearchAuthors.isNotEmpty() && !createNewAuthor && !linkToAuthor
                        ) {
                            AuthorIsNotSelectedInfo(modifier = Modifier.padding(top = 8.dp))
                        }

                        AnimatedVisibility(visible = shortBook == null && bookValues.authorName.value.text.length >= 2 && !authorIsSelected) {
                            Row {
                                NewAuthorButton(
                                    createNewAuthor = createNewAuthor,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    this@BookEditor.sendEvent(
                                        BookEditorEvents.OnChangeNeedCreateNewAuthor(
                                            !createNewAuthor
                                        )
                                    )
                                    if (createNewAuthor) {
                                        linkToAuthor = false
                                    }
                                }

                                LinkToAuthorButton(
                                    linkToAuthor = linkToAuthor,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    linkToAuthor = !linkToAuthor
                                    if (linkToAuthor) {
                                        this@BookEditor.sendEvent(
                                            BookEditorEvents.OnChangeNeedCreateNewAuthor(false)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

                if (shortBook == null && !isCreateBookManually) {
                    BookSearchSelector(
                        similarBooks = similarBooks,
                        isLoading = isSearchBookProcess,
                        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, start = 8.dp),
                        onClick = {
                            sendEvent(BookEditorEvents.OnBookSelected(it))
                        }
                    )
                }

                if (isCreateBookManually || shortBook != null) {
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
                        tagColor = bookValues.selectedStatus.value.getStatusColor()
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
                        textFieldValue = bookValues.numberOfPages
                    )

                    TextFieldWithTitleAndSuggestion(
                        platform = platform,
                        title = Strings.cover,
                        modifier = Modifier.padding(top = 2.dp),
                        hintText = Strings.hint_link_to_cover,
                        enabledInput = shortBook == null,
                        hiddenText = if (shortBook == null) null else "Вы можете добавить ссылку на свою обложку",
                        onTextChanged = {
                            bookValues.coverUrl.value = it
                        },
                        disableSingleLineIfFocused = true,
                        textFieldValue = bookValues.coverUrl
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
                        }
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
                        }
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
                        textFieldValue = bookValues.isbn
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




