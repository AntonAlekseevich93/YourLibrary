import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.AuthorsUiState

@Composable
fun BaseEventScope<BaseEvent>.AuthorsScreenContent(
    uiState: AuthorsUiState
) {
    Column(modifier = Modifier.padding(24.dp)) {
        uiState.authorByAlphabet.keys.forEach { letter ->
            AuthorFirstLetterItem(letter)

            uiState.authorByAlphabet[letter]?.forEach { author ->
                AuthorItem(author)
            }
        }
    }
}