import database.LocalReviewAndRatingDataSource
import ktor.RemoteReviewAndRatingDataSource

class ReviewAndRatingRepositoryImpl(
    private val localReviewAndRatingDataSource: LocalReviewAndRatingDataSource,
    private val remoteReviewAndRatingDataSource: RemoteReviewAndRatingDataSource,
    private val appConfig: AppConfig,
    private val remoteConfig: RemoteConfig,
) : ReviewAndRatingRepository {

}