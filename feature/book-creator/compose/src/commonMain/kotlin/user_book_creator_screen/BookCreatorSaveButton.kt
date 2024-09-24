package user_book_creator_screen

import ApplicationTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun BookCreatorSaveButton(

) {
    Button(
        onClick = {
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ApplicationTheme.colors.screenColor.activeButtonColor,
            disabledBackgroundColor = ApplicationTheme.colors.pointerIsActiveCardColor
        ),
        modifier = Modifier.fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 24.dp, bottom = 32.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Создать книгу",
            style = ApplicationTheme.typography.title3Bold,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}