package elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import book_editor.elements.authors_selector.elements.AuthorsListSelectorAuthorsList
import elements.items.ExactMatchBookInfoItem
import main_models.AuthorVo
import main_models.books.BookShortVo
import org.jetbrains.compose.resources.stringResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.exact_match
import yourlibrary.common.resources.generated.resources.nothing_fits
import yourlibrary.common.resources.generated.resources.possible_matches
import yourlibrary.common.resources.generated.resources.text_button_user_book_creator_author_not_exist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorsWithBooksSelectorBottomSheet(
    authors: List<AuthorVo>,
    exactMatchBooks: List<BookShortVo>,
    similarBooks: List<BookShortVo>,
    onDismissAuthors: () -> Unit,
    onDismissBooks: () -> Unit,
    authorSelectedListener: (AuthorVo) -> Unit,
    setSelectedBook: (book: BookShortVo) -> Unit,
) {
    BasicAlertDialog({}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier.fillMaxSize().padding(top = 65.dp),
            colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item {
                    if (exactMatchBooks.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.exact_match),
                            style = ApplicationTheme.typography.title2Bold,
                            color = ApplicationTheme.colors.mainTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp, top = 24.dp)
                        )
                    }
                }

                itemsIndexed(exactMatchBooks) { index, item ->
                    ExactMatchBookInfoItem(
                        item,
                        isLastItem = exactMatchBooks.size - 1 == index,
                        onClick = setSelectedBook
                    )
                }

                item {
                    if (exactMatchBooks.isNotEmpty()) {
                        Spacer(Modifier.padding(bottom = 10.dp))
                    }
                }

                item {
                    if (similarBooks.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.possible_matches),
                            style = ApplicationTheme.typography.title2Bold,
                            color = ApplicationTheme.colors.mainTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp, top = 24.dp)
                        )
                    }
                }

                itemsIndexed(similarBooks) { index, item ->
                    ExactMatchBookInfoItem(
                        item,
                        isLastItem = similarBooks.size - 1 == index,
                        onClick = setSelectedBook
                    )
                }

                item {
                    if (similarBooks.isNotEmpty() || exactMatchBooks.isNotEmpty())
                        Text(
                            text = stringResource(Res.string.nothing_fits),
                            style = ApplicationTheme.typography.title3Bold,
                            color = ApplicationTheme.colors.screenColor.activeButtonColor,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 36.dp, top = 46.dp)
                                .clickable(
                                    MutableInteractionSource(), null
                                ) {
                                    onDismissBooks()
                                },
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                }

                item {
                    if (authors.isNotEmpty()) {
                        Text(
                            text = stringResource(Res.string.possible_matches),
                            style = ApplicationTheme.typography.title2Bold,
                            color = ApplicationTheme.colors.mainTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp, top = 24.dp)
                        )
                    }
                }

                item {
                    AuthorsListSelectorAuthorsList(
                        authors = authors,
                        bottomButtonTitleRes = Res.string.text_button_user_book_creator_author_not_exist,
                        authorClickListener = {
                            authorSelectedListener(it)
                        },
                        bottomButtonClickListener = {
                            onDismissAuthors()
                        }
                    )
                }
            }
        }
    }
}