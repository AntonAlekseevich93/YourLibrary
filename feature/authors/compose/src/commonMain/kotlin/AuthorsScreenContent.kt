import androidx.compose.runtime.Composable
import models.AuthorsUiState

@Composable
fun AuthorsScreenContent(
    uiState: AuthorsUiState
) {
    uiState.authorByAlphabet.keys.forEach { letter ->
        AuthorFirstLetterItem(letter)

        uiState.authorByAlphabet[letter]?.forEach { author ->
            AuthorItem(author)
        }
    }
}