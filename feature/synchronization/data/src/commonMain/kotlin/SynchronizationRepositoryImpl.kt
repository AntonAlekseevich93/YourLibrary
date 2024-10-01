import database.LocalSynchronizationDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ktor.RemoteSynchronizationDataSource
import main_models.rest.authors.toAuthorVo
import main_models.rest.books.toVo
import main_models.rest.rating_review.toRemoteDto
import main_models.rest.rating_review.toVo
import main_models.rest.sync.MissingBooksAndAuthorsFromServer
import main_models.rest.sync.SynchronizeBooksWithAuthorsRequest
import main_models.rest.sync.SynchronizeReviewAndRatingContentResponse
import main_models.rest.sync.SynchronizeReviewAndRatingRequest
import main_models.rest.sync.SynchronizeServiceDevelopmentContentResponse
import main_models.rest.sync.SynchronizeUserDataRequest
import main_models.service_development.toRemoteDto

class SynchronizationRepositoryImpl(
    private val remoteSynchronizationDataSource: RemoteSynchronizationDataSource,
    private val localSynchronizationDataSource: LocalSynchronizationDataSource,
    private val bookRepository: BookInfoRepository,
    private val authorsRepository: AuthorsRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository,
    private val serviceDevelopmentRepository: ServiceDevelopmentRepository,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : SynchronizationRepository {
    private val mutex = Mutex()

    override suspend fun synchronizeUserData(): Boolean {
        return if (appConfig.isAuth) {
            synchronize()
        } else false
    }

    override suspend fun updateNotificationPushToken(pushToken: String) {
        if (appConfig.isAuth) {
            val userId = appConfig.userId.toInt()
            val deviceId = appConfig.deviceId
            val notificationData = localSynchronizationDataSource.getNotificationData(
                userId = userId,
                deviceId = deviceId
            )
            if (notificationData == null || notificationData.pushToken != pushToken) {
                remoteSynchronizationDataSource
                    .updateNotificationPushToken(pushToken)?.result?.pushToken?.let { resultToken ->
                        localSynchronizationDataSource.insertOrUpdate(
                            userId = userId,
                            deviceId = deviceId,
                            pushToken = resultToken
                        )
                    }
            }
        }
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

            response?.result?.reviewAndRatingResponse?.let {
                updateReviewAndRatingFromServer(it, userId = userId).takeIf { !success }
                    ?.let { isSuccess ->
                        success = isSuccess
                    }
            }

            response?.result?.serviceDevelopmentResponse?.let {
                updateServiceDevelopmentFromServer(it, userId = userId).takeIf { !success }
                    ?.let { isSuccess ->
                        success = isSuccess
                    }
            }
            return@withLock success
        }
    }

    private suspend fun updateBooksWithAuthorsDataFromServer(
        result: MissingBooksAndAuthorsFromServer,
        userId: Int
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
        response: SynchronizeReviewAndRatingContentResponse,
        userId: Int
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

    private suspend fun updateServiceDevelopmentFromServer(
        response: SynchronizeServiceDevelopmentContentResponse,
        userId: Int
    ): Boolean {
        var success = false
        response.missingServiceDevelopmentBooksFromServer?.let {
            it.serviceDevelopmentBooksCurrentDevice.let { currentDeviceBooks ->
                if (currentDeviceBooks.isNotEmpty()) {
                    serviceDevelopmentRepository.addOrUpdateLocalServiceDevelopmentBooksWhenSync(
                        serviceDevelopmentBooks = currentDeviceBooks.mapNotNull { it.toVo() },
                        userId = userId
                    )
                }
            }

            it.serviceDevelopmentBooksOtherDevices.mapNotNull { it.toVo() }
                .let { othersDeviceBooks ->
                    val booksTimestamp =
                        serviceDevelopmentRepository.getServiceDevelopmentBooksTimestamp(userId = userId)
                    val lastTimestampBooks = othersDeviceBooks.takeIf { it.isNotEmpty() }
                        ?.maxBy { it.timestampOfUpdating }?.timestampOfUpdating
                    if (othersDeviceBooks.isNotEmpty()) {
                        serviceDevelopmentRepository.addOrUpdateLocalServiceDevelopmentBooksWhenSync(
                            othersDeviceBooks,
                            userId = userId
                        )

                        serviceDevelopmentRepository.updateServiceDevelopmentBooksTimestamp(
                            booksTimestamp.copy(
                                otherDevicesTimestamp = lastTimestampBooks
                                    ?: booksTimestamp.otherDevicesTimestamp,
                            )
                        )
                    }
                }
            success = true
        }

        response.currentDeviceServiceDevelopmentBooksAddedToServer?.let { result ->
            val resultBooks = result.serviceDevelopmentBooks.mapNotNull { it.toVo() }
            serviceDevelopmentRepository.addOrUpdateLocalServiceDevelopmentBooksWhenSync(
                resultBooks,
                userId = userId
            )
            success = true
        }

        if (response.currentDeviceServiceDevelopmentBooksLastTimestamp != null) {
            val timestamp =
                serviceDevelopmentRepository.getServiceDevelopmentBooksTimestamp(userId = userId)
            serviceDevelopmentRepository.updateServiceDevelopmentBooksTimestamp(
                timestamp.copy(
                    thisDeviceTimestamp = response.currentDeviceServiceDevelopmentBooksLastTimestamp
                        ?: timestamp.thisDeviceTimestamp,
                )
            )
        }

        return success
    }


    /**we don't need to send authors
     * because the server creates authors on its own from the data from the book**/
    private suspend fun getSynchronizeBody(userId: Int): SynchronizeUserDataRequest {
        val booksTimestamp = bookRepository.getBookTimestamp(userId = userId)
        val authorTimestampVo = authorsRepository.getAuthorsTimestamp(userId)
        val books =
            bookRepository.getNotSynchronizedBooks(userId)
        val reviewAndRatingTimestamp = reviewAndRatingRepository.getReviewAndRatingTimestamp(userId)
        val reviewsAndRatings = reviewAndRatingRepository.getNotSynchronizedReviewAndRating(userId)
            .mapNotNull { it.toRemoteDto() }
        val serviceDevelopmentBooksTimestamp =
            serviceDevelopmentRepository.getServiceDevelopmentBooksTimestamp(userId)
        val serviceDevelopmentBooks =
            serviceDevelopmentRepository.getNotSynchronizedServiceDevelopmentBooks(userId)
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
            ),
            serviceDevelopment = null //todo разблокировать ниже
//            serviceDevelopment = SynchronizeServiceDevelopmentRequest(
//                serviceDevelopmentBooksThisDeviceTimestamp = serviceDevelopmentBooksTimestamp.thisDeviceTimestamp,
//                serviceDevelopmentBooksOtherDevicesTimestamp = serviceDevelopmentBooksTimestamp.otherDevicesTimestamp,
//                serviceDevelopmentBooks = serviceDevelopmentBooks
//            )
        )
    }
}