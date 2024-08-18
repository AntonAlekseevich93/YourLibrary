import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ktor.RemoteSynchronizationDataSource
import main_models.rest.authors.toAuthorVo
import main_models.rest.rating_review.toRemoteDto
import main_models.rest.rating_review.toVo
import main_models.rest.sync.MissingBooksAndAuthorsFromServer
import main_models.rest.sync.SynchronizeBooksWithAuthorsRequest
import main_models.rest.sync.SynchronizeReviewAndRatingContent
import main_models.rest.sync.SynchronizeReviewAndRatingRequest
import main_models.rest.sync.SynchronizeUserDataRequest

class SynchronizationRepositoryImpl(
    private val remoteSynchronizationDataSource: RemoteSynchronizationDataSource,
    private val bookRepository: BookInfoRepository,
    private val authorsRepository: AuthorsRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : SynchronizationRepository {
    private val mutex = Mutex()

    override suspend fun synchronizeUserData(): Boolean {
        return if (appConfig.isAuth) {
            synchronize()
        } else false
    }

    /**
     * У нас есть:
     * - Книги которые есть на сервере, но которых нет локально (like MissingBooksAndAuthorsFromServer).
     * - Книги которые есть локально, но нет на сервере (like CurrentDeviceBooksAndAuthorsAddedToServer).
     * - Так же с сервера возвращается последний timestamp текущего девайса (like currentDeviceBookLastTimestamp)
     * **/
    private suspend fun synchronize(): Boolean {
        return mutex.withLock {
            val userId = appConfig.userId
            val response = remoteSynchronizationDataSource.synchronizeUserData(
                body = getSynchronizeBody(userId)
            )
            var success = false
            response?.result?.booksWithAuthorsContent?.missingBooksAndAuthorsFromServer?.let { result ->
                updateBooksWithAuthorsDataFromServer(result, userId)
                success = true
            }

            response?.result?.booksWithAuthorsContent?.currentDeviceBooksAndAuthorsAddedToServer?.let { result ->
                val resultAuthors = result.authors?.mapNotNull { it.toAuthorVo() } ?: emptyList()
                bookRepository.addOrUpdateLocalBooks(result.books.orEmpty(), userId = userId)
                authorsRepository.insertOrUpdateAuthorsInLocalDb(resultAuthors)
                success = true
            }

            response?.result?.booksWithAuthorsContent?.currentDeviceBookLastTimestamp?.let {
                val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
                bookRepository.updateBookTimestamp(
                    booksTimestamp.copy(thisDeviceTimestamp = it)
                )
            }

            response?.result?.booksWithAuthorsContent?.currentDeviceAuthorLastTimestamp?.let {
                authorsRepository.updateAuthorsTimestamp(
                    thisDeviceTimestamp = it,
                    otherDeviceTimestamp = null
                )
            }

            response?.result?.reviewAndRatingContent?.let {
                updateReviewAndRatingFromServer(it, userId = userId).takeIf { !success }
                    ?.let { isSuccess ->
                        success = isSuccess
                    }
            }
            return@withLock success
        }
    }

    private suspend fun updateBooksWithAuthorsDataFromServer(
        result: MissingBooksAndAuthorsFromServer,
        userId: Long
    ) {
        result.booksOtherDevices?.let { otherDevicesBooks ->
            val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
            val lastTimestamp = otherDevicesBooks.takeIf { it.isNotEmpty() }
                ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
            if (otherDevicesBooks.isNotEmpty() && lastTimestamp != null) {
                bookRepository.addOrUpdateLocalBooks(otherDevicesBooks, userId = userId)
                bookRepository.updateBookTimestamp(
                    booksTimestamp.copy(
                        otherDevicesTimestamp = lastTimestamp
                    )
                )
            }
        }

        result.authorsOtherDevices?.let { otherDevicesAuthors ->
            val lastTimestamp = otherDevicesAuthors.takeIf { it.isNotEmpty() }
                ?.maxBy { it.timestampOfUpdating!! }?.timestampOfUpdating
            if (otherDevicesAuthors.isNotEmpty() && lastTimestamp != null) {
                val authorsVo = otherDevicesAuthors.mapNotNull { it.toAuthorVo() }
                authorsRepository.insertOrUpdateAuthorsInLocalDb(authorsVo)
                authorsRepository.updateAuthorsTimestamp(
                    thisDeviceTimestamp = null,
                    otherDeviceTimestamp = lastTimestamp
                )
            }
        }

        result.booksCurrentDevice?.let { currentDevicesBooks ->
            if (currentDevicesBooks.isNotEmpty()) {
                bookRepository.addOrUpdateLocalBooks(currentDevicesBooks, userId = userId)
                runCatching { currentDevicesBooks.maxBy { it.timestampOfUpdating!! } }.getOrNull()?.timestampOfUpdating?.let {
                    val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
                    bookRepository.updateBookTimestamp(
                        booksTimestamp.copy(
                            thisDeviceTimestamp = it
                        )
                    )
                }
            }
        }

        result.authorsCurrentDevice?.let { otherDevicesAuthors ->
            if (otherDevicesAuthors.isNotEmpty()) {
                val authorsVo = otherDevicesAuthors.mapNotNull { it.toAuthorVo() }
                authorsRepository.insertOrUpdateAuthorsInLocalDb(authorsVo)
            }
        }
    }

    private suspend fun updateReviewAndRatingFromServer(
        response: SynchronizeReviewAndRatingContent,
        userId: Long
    ): Boolean {
        var success = false
        response.missingReviewsAndRatingsFromServer?.let {
            it.reviewsAndRatingCurrentDevice.let { currentDeviceReviewAndRating ->
                if (currentDeviceReviewAndRating.isNotEmpty()) {
                    reviewAndRatingRepository.addOrUpdateLocalReviewAndRatingWhenSync(
                        currentDeviceReviewAndRating.mapNotNull { it.toVo() },
                        userId = userId
                    )
                }
            }

            it.reviewsAndRatingOtherDevices.let { othersDeviceReviewAndRating ->
                val reviewAndRatingTimestamp =
                    reviewAndRatingRepository.getReviewAndRatingTimestamp(userId = userId)
                val lastTimestampReview = othersDeviceReviewAndRating.takeIf { it.isNotEmpty() }
                    ?.maxBy { it.timestampOfUpdatingScore }?.timestampOfUpdatingScore
                val lastTimestampRating = othersDeviceReviewAndRating.takeIf { it.isNotEmpty() }
                    ?.maxBy { it.timestampOfUpdatingReview }?.timestampOfUpdatingReview
                if (othersDeviceReviewAndRating.isNotEmpty()) {
                    reviewAndRatingRepository.addOrUpdateLocalReviewAndRatingWhenSync(
                        othersDeviceReviewAndRating.mapNotNull { it.toVo() },
                        userId = userId
                    )

                    reviewAndRatingRepository.updateReviewAndRatingTimestamp(
                        reviewAndRatingTimestamp.copy(
                            otherDevicesTimestampReview = lastTimestampReview
                                ?: reviewAndRatingTimestamp.otherDevicesTimestampReview,
                            otherDevicesTimestampRating = lastTimestampRating
                                ?: reviewAndRatingTimestamp.otherDevicesTimestampRating
                        )
                    )
                }
            }
            success = true
        }

        response.currentDeviceReviewsAndRatingsAddedToServer?.let { result ->
            val resultReviewsAndRating = result.reviewsAndRating.mapNotNull { it.toVo() }
            reviewAndRatingRepository.addOrUpdateLocalReviewAndRatingWhenSync(
                resultReviewsAndRating,
                userId = userId
            )
            success = true
        }

        if (response.currentDeviceReviewLastTimestamp != null || response.currentDeviceRatingLastTimestamp != null) {
            val timestamp = reviewAndRatingRepository.getReviewAndRatingTimestamp(userId = userId)
            reviewAndRatingRepository.updateReviewAndRatingTimestamp(
                timestamp.copy(
                    thisDeviceTimestampRating = response.currentDeviceRatingLastTimestamp
                        ?: timestamp.thisDeviceTimestampRating,
                    thisDeviceTimestampReview = response.currentDeviceReviewLastTimestamp
                        ?: timestamp.thisDeviceTimestampReview
                )
            )
        }

        return success
    }

    private suspend fun getSynchronizeBody(userId: Long): SynchronizeUserDataRequest {
        val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
        val authorTimestampVo = authorsRepository.getAuthorsTimestamp(userId)
        val books =
            bookRepository.getNotSynchronizedBooks(userId) //todo нужно еще не синхронизированных авторов получать
        val reviewAndRatingTimestamp = reviewAndRatingRepository.getReviewAndRatingTimestamp(userId)
        val reviewsAndRatings = reviewAndRatingRepository.getNotSynchronizedReviewAndRating(userId)
            .mapNotNull { it.toRemoteDto() }
        return SynchronizeUserDataRequest(
            booksWithAuthors = SynchronizeBooksWithAuthorsRequest(
                booksThisDeviceTimestamp = booksTimestamp.thisDeviceTimestamp,
                booksOtherDevicesTimestamp = booksTimestamp.otherDevicesTimestamp,
                authorsThisDeviceTimestamp = authorTimestampVo.thisDeviceTimestamp,
                authorsOtherDevicesTimestamp = authorTimestampVo.otherDevicesTimestamp,
                books = books
            ),
            reviewsAndRatings = SynchronizeReviewAndRatingRequest(
                reviewThisDeviceTimestamp = reviewAndRatingTimestamp.thisDeviceTimestampReview,
                reviewOtherDevicesTimestamp = reviewAndRatingTimestamp.otherDevicesTimestampReview,
                ratingThisDeviceTimestamp = reviewAndRatingTimestamp.thisDeviceTimestampRating,
                ratingOtherDevicesTimestamp = reviewAndRatingTimestamp.otherDevicesTimestampRating,
                reviewAndRatings = reviewsAndRatings
            )
        )
    }
}