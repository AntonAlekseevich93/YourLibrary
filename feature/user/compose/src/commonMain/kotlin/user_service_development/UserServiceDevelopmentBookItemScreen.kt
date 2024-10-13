package user_service_development

import ApplicationTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.request.error
import com.github.panpf.sketch.request.placeholder
import com.github.panpf.sketch.resize.Scale
import main_models.service_development.BookWithServiceDevelopment
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.ic_default_book_cover_7

@Composable
fun UserServiceDevelopmentBookItemScreen(
    bookWithServiceDevelopment: BookWithServiceDevelopment,
    asMock: Boolean = false,
    openBookInfoListener: () -> Unit,
    openEditBookListener: () -> Unit,
) {
    val imageModifier = Modifier.sizeIn(
        minHeight = 140.dp,
        minWidth = 88.dp,
        maxHeight = 140.dp,
        maxWidth = 88.dp
    )
    Column {
        Row(
            modifier = Modifier
                .clickable(MutableInteractionSource(), null) {
                    bookWithServiceDevelopment.serviceDevelopmentBookVo.apply {
                        if (isApproved || !isModerated) {
                            openBookInfoListener()
                        } else {
                            openEditBookListener()
                        }
                    }
                }
                .padding(
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Card(
                modifier = imageModifier,
                colors = CardDefaults.cardColors(ApplicationTheme.colors.cardBackgroundDark),
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                if (!asMock) {
                    AsyncImage(
                        modifier = imageModifier,
                        request = ComposableImageRequest(bookWithServiceDevelopment.book.remoteImageLink) {
                            scale(Scale.FILL)
                            placeholder(Res.drawable.ic_default_book_cover_7)
                            error(Res.drawable.ic_default_book_cover_7)
                        },
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                    )
                }
            }

            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = bookWithServiceDevelopment.book.bookName,
                    style = ApplicationTheme.typography.headlineBold,
                    color = ApplicationTheme.colors.mainTextColor,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                Text(
                    text = bookWithServiceDevelopment.book.originalAuthorName,
                    style = ApplicationTheme.typography.bodyRegular,
                    color = ApplicationTheme.colors.mainTextColor,
                )

                ServiceDevelopmentStatus(
                    isModerated = bookWithServiceDevelopment.serviceDevelopmentBookVo.isModerated,
                    isApproved = bookWithServiceDevelopment.serviceDevelopmentBookVo.isApproved
                )
            }
        }
    }
}