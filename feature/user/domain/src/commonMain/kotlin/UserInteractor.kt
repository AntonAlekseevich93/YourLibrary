import kotlinx.coroutines.flow.Flow
import main_models.books.UserBooksStatisticsData
import main_models.rating_review.ReviewAndRatingVo
import main_models.rest.users.AuthRegisterRequest
import main_models.rest.users.AuthRequest
import main_models.user.UserVo

class UserInteractor(
    private val repository: UserRepository,
    private val searchRepository: SearchRepository,
    private val appConfig: AppConfig,
    private val booksRepository: BookInfoRepository,
    private val reviewAndRatingRepository: ReviewAndRatingRepository
) {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ) {
        val response = repository.signUp(
            request = AuthRegisterRequest(
                userName = name,
                userEmail = email,
                password = password
            )
        )

        response?.result?.token?.let { token ->
            if (token.isNotEmpty() && response.result?.id != null) {
                appConfig.updateAuthToken(token = token)
                repository.createOrUpdateUser(
                    id = response.result!!.id!!,
                    name = name,
                    email = email,
                    isVerified = response.result?.isVerified ?: false,
                    isAuthorized = true,
                    isModerator = false,
                )
            }
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ) {
        val response = repository.signIn(
            request = AuthRequest(
                userEmail = email,
                password = password
            )
        )

        response?.result?.token?.let { token ->
            if (token.isNotEmpty()) {
                appConfig.updateAuthToken(token = token)
                response.result?.let {
                    it.id?.let { id ->
                        repository.createOrUpdateUser(
                            id = it.id!!,
                            name = it.name!!,
                            email = email,
                            isVerified = response.result?.isVerified ?: false,
                            isAuthorized = true,
                            isModerator = it.isModerator ?: false,
                        )
                    }
                }
            }
        }
    }

    suspend fun getAuthorizedUser(): Flow<UserVo?> =
        repository.getAuthorizedUserFlow()

    suspend fun logOut() {
        repository.logOut()
    }

    suspend fun updateUserStatus() {
        repository.getUserStatus()?.let { userStatus ->
            if (!userStatus.tokenExist) {
                repository.logOut()
            }
            appConfig.changeUserModeratorStatus(userStatus.isModerator)
        }
    }

    suspend fun updateUserInfo() {
        repository.updateUserInfo()
    }

    suspend fun getUserBooksStatistics(): Flow<UserBooksStatisticsData> =
        booksRepository.getUserBooksStatistics()

    suspend fun getFinishedThisYearCountBooks(): Flow<Int> =
        booksRepository.getFinishedThisYearCountBooks()

    suspend fun getUserReviews(): Flow<List<ReviewAndRatingVo>> =
        reviewAndRatingRepository.getAllCurrentUserReviews()

    suspend fun updateUserReadingGoalsInCurrentYear(goal: Int) {
        repository.updateUserReadingGoalsInCurrentYear(goal)
    }

}