package join_authors_screen.components

import ApplicationTheme
import BaseEvent
import BaseEventScope
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import join_authors_screen.elements.AuthorButtonContainer
import main_models.AuthorVo

@Composable
fun BaseEventScope<BaseEvent>.JoinRelatesAuthorBlock(mainAuthor: State<AuthorVo>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.authorsContainerColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = Strings.relatesAuthors,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.hintColor,
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
            )

            mainAuthor.value.relatedAuthors.forEach { author ->
                var showDropdown by remember { mutableStateOf(false) }
                Box {
                    AuthorButtonContainer(
                        text = author.name,
                        color = ApplicationTheme.colors.joinAuthorsColors.relatesAuthorsColor
                    ) {
                        showDropdown = true
                    }
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = {
                            showDropdown = false
                        },
                        modifier = Modifier.background(ApplicationTheme.colors.mainBackgroundColor),
                        content = {
                            RelatesAuthorsDropMenu(
                                mainAuthor = mainAuthor.value,
                                authorVo = author,
                                onClose = {
                                    showDropdown = false
                                }
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }
}

