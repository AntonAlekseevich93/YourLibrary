package book_editor

import ApplicationTheme
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.DatePickerType
import main_models.ReadingStatus
import platform.Platform
import platform.isMobile
import reading_status.getStatusColor
import tags.CustomTag
import text_fields.TextFieldWithTitleAndSuggestion

@Composable
fun BookEditor(
    platform: Platform,
    bookValues: BookValues,
    similarAuthorList: State<List<String>>,
    statusBookTextFieldValue: MutableState<TextFieldValue>,
    showDataPickerListener: (type: DatePickerType) -> Unit,
    onAuthorTextChanged: (TextFieldValue) -> Unit,
    onSuggestionAuthorClickListener: (author: String) -> Unit,
    saveBook: () -> Unit,
    modifier: Modifier = Modifier,
    buttonTitle: String = Strings.add_book,
) {
    val showImage = remember { mutableStateOf(false) }
    val painter =
        asyncPainterResource(
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

        AnimatedVisibility(
            visible = bookValues.bookName.value.text.isNotEmpty() &&
                    bookValues.authorName.value.text.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            CenterBoxContainer(modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)) {
                CustomTag(
                    text = buttonTitle,
                    color = ApplicationTheme.colors.mainAddButtonColor,
                    textStyle = ApplicationTheme.typography.footnoteBold,
                    textModifier = Modifier.padding(vertical = 8.dp),
                    maxHeight = 50.dp,
                    onClick = saveBook
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TextFieldWithTitleAndSuggestion(
                    platform = platform,
                    title = Strings.autor,
                    modifier = Modifier,
                    hintText = Strings.hint_type_author,
                    textFieldValue = bookValues.authorName,
                    onTextChanged = onAuthorTextChanged,
                    suggestionList = similarAuthorList,
                    suggestionMaxHeight = 120.dp,
                    onSuggestionClickListener = onSuggestionAuthorClickListener,
                )

                TextFieldWithTitleAndSuggestion(
                    platform = platform,
                    title = Strings.title,
                    modifier = Modifier.padding(top = 2.dp),
                    hintText = Strings.hint_type_title_book,
                    onTextChanged = {
                        bookValues.bookName.value = it
                    },
                    disableSingleLineIfFocused = true,
                    textFieldValue = bookValues.bookName
                )

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
                        showDataPickerListener.invoke(DatePickerType.StartDate)
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
                        showDataPickerListener.invoke(DatePickerType.EndDate)
                    }
                )

                TextFieldWithTitleAndSuggestion(
                    platform = platform,
                    title = Strings.description,
                    modifier = Modifier.padding(top = 2.dp),
                    hintText = Strings.hint_book_description,
                    onTextChanged = {
                        bookValues.description.value = it
                    },
                    disableSingleLineIfFocused = true,
                    textFieldValue = bookValues.description,
                    maxLines = 40
                )

                TextFieldWithTitleAndSuggestion(
                    platform = platform,
                    title = Strings.isbn,
                    modifier = Modifier.padding(top = 2.dp),
                    hintText = Strings.hint_isbn_number,
                    onTextChanged = {
                        bookValues.isbn.value = it
                    },
                    textFieldValue = bookValues.isbn
                )

                /**this use for add padding if keyboard shown**/
                if (platform.isMobile()) {
                    Spacer(Modifier.padding(200.dp))
                } else {
                    Spacer(Modifier.padding(30.dp))
                }
            }
        }
    }
}