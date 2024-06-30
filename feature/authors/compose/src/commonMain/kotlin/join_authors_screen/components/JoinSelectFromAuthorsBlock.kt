package join_authors_screen.components

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable
import Strings
import alert_dialog.CommonAlertDialogConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import authors_screen.AuthorFirstLetterItem
import authors_screen.AuthorItem
import authors_screen.AuthorItemMenu
import containters.CenterBoxContainer
import main_models.AuthorVo
import models.AuthorsEvents
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_search_glass

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BaseEventScope<BaseEvent>.JoinAllAuthors(
    originalAuthor: State<AuthorVo>,
    authorsByAlphabet: LinkedHashMap<String, MutableList<AuthorVo>>,
    searchingResult: State<LinkedHashMap<String, MutableList<AuthorVo>>>,
    showAlertDialog: (config: CommonAlertDialogConfig, newAuthorVo: AuthorVo) -> Unit,
) {
    var searchText by remember { mutableStateOf("") }
    val hideAuthorsMenu = remember { mutableStateOf("") }

    CenterBoxContainer {
        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            query = searchText,
            onQueryChange = {
                searchText = it
                if (searchText.trim().isEmpty()) {
                    this@JoinAllAuthors.sendEvent(AuthorsEvents.FinishSearch)
                } else {
                    this@JoinAllAuthors.sendEvent(
                        AuthorsEvents.OnSearch(
                            searchText,
                            originalAuthor.value.id
                        )
                    )
                }
            },
            onSearch = {

            },
            active = false,
            onActiveChange = {},
            colors = SearchBarDefaults.colors(
                containerColor = ApplicationTheme.colors.searchBackgroundDark,
                inputFieldColors = TextFieldDefaults.colors(focusedTextColor = Color.White)
            ),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.ic_search_glass),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ApplicationTheme.colors.searchIconColor),
                    modifier = Modifier.size(20.dp)
                )
            },
            placeholder = {
                Text(
                    text = Strings.search_by_author,
                    modifier = Modifier,
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor
                )
            }
        ) {
            //nop
        }
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.authorsContainerColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
    ) {

        CenterBoxContainer {
            Text(
                text = Strings.all_authors,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.hintColor,
                modifier = Modifier.padding(top = 12.dp, start = 24.dp, bottom = 16.dp)
            )
        }

        if (searchText.isEmpty()) {
            authorsByAlphabet.keys.forEach { letter ->

                AuthorFirstLetterItem(letter, Modifier.padding(start = 8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(bottom = 12.dp, top = 4.dp)) {
                        authorsByAlphabet[letter]?.forEach { modifiedAuthor ->
                            AuthorItem(
                                author = modifiedAuthor,
                                showAuthorMenuEvent = { hideAuthorsMenu.value = it },
                                hideAuthorMenu = hideAuthorsMenu,
                                contentModifier = Modifier.padding(start = 8.dp),
                                menuContent = {
                                    JoinInSearchItemClickMenu(
                                        originalAuthor = originalAuthor,
                                        modifiedAuthor = modifiedAuthor,
                                        onClickAsMain = {
                                            showAlertDialog.invoke(
                                                CommonAlertDialogConfig(
                                                    title = Strings.as_main_author_alert_dialog_title,
                                                    description = Strings.as_main_author_alert_dialog_description.format(
                                                        originalAuthor.value.name,
                                                        modifiedAuthor.name
                                                    ),
                                                    acceptButtonTitle = Strings.as_main_author_alert_dialog_accept_button.format(
                                                        modifiedAuthor.name
                                                    ),
                                                    dismissButtonTitle = Strings.cancel
                                                ),
                                                modifiedAuthor
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        } else {
            searchingResult.value.keys.forEach { letter ->

                AuthorFirstLetterItem(letter, Modifier.padding(start = 8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        searchingResult.value[letter]?.forEach { author ->
                            AuthorItem(
                                author = author,
                                showAuthorMenuEvent = { hideAuthorsMenu.value = it },
                                hideAuthorMenu = hideAuthorsMenu,
                                contentModifier = Modifier.padding(start = 8.dp),
                                menuContent = {
                                    AuthorItemMenu(author)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}