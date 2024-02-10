package authors_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.AuthorsUiState

@Composable
fun BaseEventScope<BaseEvent>.AuthorsScreenContent(
    uiState: AuthorsUiState
) {
    val hideAuthorsMenu = remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.padding(24.dp)) {
        uiState.authorByAlphabet.keys.forEach { letter ->
            item {
                AuthorFirstLetterItem(letter)
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 12.dp)) {
                        uiState.authorByAlphabet[letter]?.forEach { author ->
                            AuthorItem(
                                author = author,
                                showAuthorMenuEvent = { hideAuthorsMenu.value = it },
                                hideAuthorMenu = hideAuthorsMenu
                            )
                        }
                    }
                }
            }
        }
    }
}