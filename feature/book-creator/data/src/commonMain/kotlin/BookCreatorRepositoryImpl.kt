import database.LocalBookCreatorDataSource

class BookCreatorRepositoryImpl(
    private val localBookCreatorDataSource: LocalBookCreatorDataSource
) : BookCreatorRepository {

}