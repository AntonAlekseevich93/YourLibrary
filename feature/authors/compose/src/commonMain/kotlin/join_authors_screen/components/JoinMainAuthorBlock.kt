package join_authors_screen.components

import ApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import join_authors_screen.elements.AuthorButtonContainer
import main_models.AuthorVo

@Composable
fun JoinMainAuthorBlock(authorVo: State<AuthorVo>) {
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
                text = Strings.main_author,
                style = ApplicationTheme.typography.bodyBold,
                color = ApplicationTheme.colors.hintColor,
                modifier = Modifier.padding(top = 12.dp)
            )
            AuthorButtonContainer(
                text = authorVo.value.name,
                Modifier.padding(bottom = 24.dp, top = 16.dp)
            )
        }
    }
}

