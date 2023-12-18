import database.LocalBookDataSource

class BookRepositoryImpl(
    private val localBookDataSource: LocalBookDataSource
) : BookRepository {

}