import database.LocalBookInfoDataSource

class BookInfoRepositoryImpl(
    private val localBookInfoDataSource: LocalBookInfoDataSource
) : BookInfoRepository {

}