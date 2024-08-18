package book_info

import ApplicationTheme
import BaseEvent
import BaseEventScope
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
import main_models.rating_review.ReviewAndRatingVo
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
fun BaseEventScope<BaseEvent>.BookInfoAboutBook(
    description: String,
    genre: String,
    ageRestrictions: String?,
    pageCount: Int,
    startDate: String?,
    endDate: String?,
    readingDayAmount: Int?,
    allUsersRating: Double,
    allRatingAmount: Int,
    userReviewAndRating: State<ReviewAndRatingVo?>,
    reviewsAndRatings: State<List<ReviewAndRatingVo>>,
    reviewsCount: State<Int>,
    otherBooksByAuthor: State<List<BookShortVo>>,
    reviewButtonPosition: (position: Int) -> Unit,
    onWriteReviewListener: () -> Unit,
    scrollToReviewButtonListener: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            BookRatingMiniBlock(
                allUsersRating = allUsersRating,
                allRatingAmount = allRatingAmount,
                currentUserScore = userReviewAndRating.value?.ratingScore,
                scrollToReviewButtonListener = scrollToReviewButtonListener,
            )

            Spacer(Modifier.padding(16.dp))

            if (pageCount > 0) {
                BookInfoCommonItem(
                    title = pageCount.toString(),
                    description = pluralStringResource(Res.plurals.page_count, pageCount)
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
            reviewCount = allRatingAmount,
            averageRating = allUsersRating,
            modifier = Modifier.padding(top = 36.dp, bottom = 12.dp)
        )

        AboutRating(
            showReviewButton = userReviewAndRating.value?.reviewText == null,
            reviewButtonPosition = reviewButtonPosition,
            userRating = userReviewAndRating.value?.ratingScore ?: 0,
            onClickReviewButton = onWriteReviewListener,
        )

        if (reviewsCount.value > 0) {
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Отзывы",
                    style = ApplicationTheme.typography.title2Bold,
                    color = ApplicationTheme.colors.mainTextColor,

                    )
                if (reviewsAndRatings.value.size > 1) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Все ${reviewsCount.value}",
                        style = ApplicationTheme.typography.headlineBold,
                        color = ApplicationTheme.colors.screenColor.activeLinkColor,
                    )
                }
            }

            ReviewHorizontalList(
                reviews = reviewsAndRatings,
                modifier = Modifier.padding(top = 16.dp),
                allReviewCount = reviewsAndRatings.value.size,
                showAllReviewListener = {

                }
            )
        }

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
