package elements

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import main_models.AuthorVo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorsSelectorBottomSheet(authors: List<AuthorVo>, onDismiss: () -> Unit) {
    BasicAlertDialog({}, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier.fillMaxSize().padding(top = 65.dp),
            colors = CardDefaults.cardColors(containerColor = ApplicationTheme.colors.cardBackgroundLight),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthorsVerticalListSelector(authors: List<AuthorVo>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(authors) { item ->
            AuthorItem(item) {
            }
        }
        item {
            Column(
                modifier = Modifier.padding(top = 36.dp, bottom = 36.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ничего не найдено?",
                    style = ApplicationTheme.typography.title3Bold,
                    color = ApplicationTheme.colors.screenColor.activeButtonColor,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
internal fun AuthorItem(
    author: AuthorVo,
    onAuthorClick: (author: AuthorVo) -> Unit,

    ) {
    Column(modifier = Modifier
        .clickable {
            onAuthorClick(author)
        }) {
        Text(
            text = author.name,
            style = ApplicationTheme.typography.headlineRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )


    }

}