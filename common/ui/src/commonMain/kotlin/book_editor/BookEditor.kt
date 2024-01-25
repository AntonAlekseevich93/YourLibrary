package book_editor

import ApplicationTheme
import Strings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import info.InfoBlock
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import main_models.AuthorVo
import main_models.DatePickerType
import main_models.ReadingStatus
import platform.Platform
import platform.isMobile
import reading_status.getStatusColor
import text_fields.DELAY_FOR_LISTENER_PROCESSING
import text_fields.DropdownSuggestionItem
import text_fields.TextFieldWithTitleAndSuggestion

@Composable
fun BookEditor(
    platform: Platform,
    bookValues: BookValues,
    similarSearchAuthors: SnapshotStateList<AuthorVo>,
    selectedAuthor: State<AuthorVo?>,
    statusBookTextFieldValue: MutableState<TextFieldValue>,
    isKeyboardShown: State<Boolean>,
    createNewAuthor: MutableState<Boolean>,
    linkToAuthor: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    canShowError: Boolean = false,
    onAuthorTextChanged: (newValue: TextFieldValue, textWasChanged: Boolean) -> Unit,
    onSuggestionAuthorClickListener: (author: AuthorVo) -> Unit,
    showDataPickerListener: (type: DatePickerType) -> Unit,
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

    val authorIsSelected by remember(key1 = selectedAuthor.value) { mutableStateOf(selectedAuthor.value != null) }

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
                    title = Strings.autor,
                    hintText = Strings.hint_type_author,
                    textFieldValue = bookValues.authorName,
                    onTextChanged = {
                        val oldText = bookValues.authorName.value.text
                        bookValues.authorName.value = it
                        onAuthorTextChanged.invoke(it, oldText != it.text)
                    },
                    setAsSelected = !authorIsSelected && similarSearchAuthors.isNotEmpty() && !createNewAuthor.value && !linkToAuthor.value,
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
                            visible = similarSearchAuthors.isNotEmpty() && selectedAuthor.value == null && !createNewAuthor.value && !linkToAuthor.value,
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
                                                onSuggestionAuthorClickListener.invoke(author)
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = (platform.isMobile() || canShowError) && selectedAuthor.value == null &&
                                    similarSearchAuthors.isNotEmpty() && !createNewAuthor.value && !linkToAuthor.value
                        ) {
                            AuthorIsNotSelectedInfo(modifier = Modifier.padding(top = 8.dp))
                        }

                        AnimatedVisibility(visible = bookValues.authorName.value.text.length >= 2 && !authorIsSelected) {
                            Row {
                                NewAuthorButton(
                                    createNewAuthor = createNewAuthor.value,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    createNewAuthor.value = !createNewAuthor.value
                                    if (createNewAuthor.value) {
                                        linkToAuthor.value = false
                                    }
                                }

                                LinkToAuthorButton(
                                    linkToAuthor = linkToAuthor.value,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    linkToAuthor.value = !linkToAuthor.value
                                    if (linkToAuthor.value) {
                                        createNewAuthor.value = false
                                    }
                                }
                            }
                        }
                    }
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
                    if (isKeyboardShown.value) {
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

@Composable
fun AuthorIsNotSelectedInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoBlock(
            text = Strings.error_author_can_exist,
            textColor = ApplicationTheme.colors.errorColor,
        )
    }
}


@Composable
fun NewAuthorButton(
    createNewAuthor: Boolean,
    modifier: Modifier = Modifier,
    createNewAuthorButtonListener: () -> Unit,
) {
    Card(
        modifier = modifier
            .padding(end = 16.dp, top = 8.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                createNewAuthorButtonListener.invoke()
            },
        colors = CardDefaults.cardColors(ApplicationTheme.colors.mainBackgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = if (createNewAuthor)
                    Icons.Default.CheckBox
                else
                    Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (createNewAuthor)
                    ApplicationTheme.colors.primaryButtonColor
                else
                    ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier.padding().size(20.dp),
            )

            Text(
                text = Strings.create_new_author,
                style = ApplicationTheme.typography.footnoteRegular,
                color = if (createNewAuthor)
                    ApplicationTheme.colors.primaryButtonColor
                else ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp)
            )
        }
    }
}

@Composable
fun LinkToAuthorButton(
    linkToAuthor: Boolean,
    modifier: Modifier = Modifier,
    createNewAuthorButtonListener: () -> Unit,
) {
    Card(
        modifier = modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                createNewAuthorButtonListener.invoke()
            },
        colors = CardDefaults.cardColors(ApplicationTheme.colors.mainBackgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (linkToAuthor)
                    Icons.Default.CheckBox
                else
                    Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (linkToAuthor)
                    ApplicationTheme.colors.primaryButtonColor
                else
                    ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier.padding().size(20.dp),
            )

            Text(
                text = Strings.link_to_author,
                style = ApplicationTheme.typography.footnoteRegular,
                color = if (linkToAuthor)
                    ApplicationTheme.colors.primaryButtonColor
                else ApplicationTheme.colors.mainTextColor,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp)
            )
        }
    }
}

