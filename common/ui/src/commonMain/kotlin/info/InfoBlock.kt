package info

import ApplicationTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InfoBlock(
    text: String,
    textColor: Color = ApplicationTheme.colors.mainTextColor,
    background: Color = ApplicationTheme.colors.mainBackgroundColor,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = ApplicationTheme.typography.footnoteRegular,
    textAlign: TextAlign? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = ApplicationTheme.colors.mainIconsColor,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(26.dp)
            )

            Text(
                text = text,
                style = textStyle,
                color = textColor,
                textAlign = textAlign
            )
        }
    }
}