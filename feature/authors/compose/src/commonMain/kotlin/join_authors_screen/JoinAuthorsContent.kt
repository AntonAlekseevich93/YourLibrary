package join_authors_screen

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Drawable.drawable_ic_arrow_in_box
import Drawable.drawable_ic_join
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import containters.CenterBoxContainer
import join_authors_screen.components.JoinMainAuthorBlock
import join_authors_screen.components.JoinRelatesAuthorBlock
import join_authors_screen.components.JoinAllAuthors
import main_models.AuthorVo
import models.JoiningAuthorsUiState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BaseEventScope<BaseEvent>.JoinAuthorsContent(
    state: State<JoiningAuthorsUiState>,
    searchingAuthorsResult: State<LinkedHashMap<String, MutableList<AuthorVo>>>
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        JoinMainAuthorBlock(state.value.mainAuthor)

        CenterBoxContainer {
            Image(
                painter = painterResource(drawable_ic_join),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                modifier = Modifier
                    .rotate(90f)
                    .padding(vertical = 8.dp)
                    .size(36.dp)
            )
        }

        JoinRelatesAuthorBlock(state.value.mainAuthor)

        CenterBoxContainer {
            Image(
                painter = painterResource(drawable_ic_arrow_in_box),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ApplicationTheme.colors.mainIconsColor),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(34.dp)
            )
        }

        JoinAllAuthors(
            mainAuthor = state.value.mainAuthor,
            authorByAlphabet = state.value.allAuthorsExceptMainAndRelates,
            searchingResult = searchingAuthorsResult
        )
    }
}