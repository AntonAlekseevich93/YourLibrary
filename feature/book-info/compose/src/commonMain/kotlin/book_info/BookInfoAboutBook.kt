package book_info

import ApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import book_info.elements.AboutRating
import book_info.elements.BookInfoCommonItem
import main_models.books.BookShortVo
import main_models.review.ReviewVo
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import rating.elements.BookRatingMiniBlock
import review.ReviewHorizontalList
import review.elements.ReviewAmountInfoElement
import text.ExpandableText
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.about_book
import yourlibrary.common.resources.generated.resources.age
import yourlibrary.common.resources.generated.resources.genre
import yourlibrary.common.resources.generated.resources.page_count
import yourlibrary.common.resources.generated.resources.read_in_days


@Composable
fun BookInfoAboutBook(
    description: String,
    genre: String,
    ageRestrictions: String?,
    pageCount: Int,
    startDate: String?,
    endDate: String?,
    readingDayAmount: Int?,
    currentUserScore: Int?,
    userReview: String?, //todo fix to object
    otherBooksByAuthor: State<List<BookShortVo>>,
    onWriteReviewListener: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            BookRatingMiniBlock(
                allUsersRating = 4.3,
                allRatingAmount = 13,
                currentUserScore = 4
            )

            Spacer(Modifier.padding(16.dp))

            if (pageCount > 0) {
                BookInfoCommonItem(
                    title = pageCount.toString(),
                    description = pluralStringResource(Res.plurals.page_count, pageCount, pageCount)
                )
                Spacer(Modifier.padding(16.dp))
            }
            if (!ageRestrictions.isNullOrEmpty()) {
                BookInfoCommonItem(
                    title = ageRestrictions,
                    description = stringResource(Res.string.age).lowercase()
                )
            }
        }
        if (!startDate.isNullOrEmpty() && !endDate.isNullOrEmpty() && readingDayAmount != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            ) {
                Text(
                    text = pluralStringResource(
                        Res.plurals.read_in_days,
                        readingDayAmount,
                        readingDayAmount
                    ),
                    style = ApplicationTheme.typography.footnoteRegular,
                    color = ApplicationTheme.colors.hintColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    val resText = "($startDate — $endDate)"
                    Text(
                        text = resText,
                        style = ApplicationTheme.typography.footnoteRegular,
                        color = ApplicationTheme.colors.hintColor,
                    )
                }
            }
        }

        Text(
            text = stringResource(Res.string.about_book),
            style = ApplicationTheme.typography.title2Bold,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 24.dp)
        )

        ExpandableText(
            text = description,
            collapsedMaxLine = 5,
            modifier = Modifier.padding(horizontal = 16.dp),
            showMoreStyle = SpanStyle(color = Color(0xFFedf6f9), fontWeight = FontWeight.Bold)
        )

        Text(
            text = stringResource(Res.string.genre),
            style = ApplicationTheme.typography.title2Bold,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp)
        )

        Text(
            text = genre,
            style = ApplicationTheme.typography.headlineRegular,
            color = ApplicationTheme.colors.mainTextColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ReviewAmountInfoElement(
            reviewCount = 5,
            averageRating = 4.2,
            modifier = Modifier.padding(top = 36.dp, bottom = 12.dp)
        )

        AboutRating(
            showReviewButton = true, //todo,
            onClickReviewButton = onWriteReviewListener
        )

        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Отзывы",
                style = ApplicationTheme.typography.title2Bold,
                color = ApplicationTheme.colors.mainTextColor,

                )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Все 10",
                style = ApplicationTheme.typography.headlineBold,
                color = ApplicationTheme.colors.screenColor.activeLinkColor,
            )
        }

        ReviewHorizontalList(
            reviews = listOf(
                ReviewVo(
                    reviewText = "Своеобразная книга. Хотелось бы сказать что нибудь в целом идеально, но книга показалась нудной. И временами мне хотелось просто ее закрыть и больше не открывать. Но я пересилила себя. Взяла в руки и в целом я довольна но не рекомендую",
                    userName = "Александрийская Анна Александровна",
                    date = "27 апреля 2024",
                    rating = 3,
                    likeCount = 2,
                    dislikeCount = 0,
                ),

                ReviewVo(
                    reviewText = "Ну что я могу сказать. Не плохо. Довольно даже не плохо И временами мне хотелось просто ее закрыть и больше не открывать",
                    userName = "Иван",
                    date = "1 января 2022",
                    rating = 4,
                    likeCount = 0,
                    dislikeCount = 0,
                )
            ),
            modifier = Modifier.padding(top = 16.dp),
            allReviewCount = 10,
            showAllReviewListener = {}
        )

        if (otherBooksByAuthor.value.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Другие книги автора",
                    style = ApplicationTheme.typography.title2Bold,
                    color = ApplicationTheme.colors.mainTextColor,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Все ${otherBooksByAuthor.value.size}",
                    style = ApplicationTheme.typography.headlineBold,
                    color = ApplicationTheme.colors.screenColor.activeLinkColor,
                )
            }

            BooksHorizontalSlider(
                books = otherBooksByAuthor,
                allBooksCount = otherBooksByAuthor.value.size, //todo fix
                modifier = Modifier.padding(top = 10.dp, bottom = 16.dp),
                showAllBooksListener = {

                }
            )
        }
    }
}
