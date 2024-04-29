package book_editor.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

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
