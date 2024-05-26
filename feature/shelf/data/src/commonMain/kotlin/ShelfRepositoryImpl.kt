import database.LocalShelfDataSource
import ktor.RemoteShelfDataSource

class ShelfRepositoryImpl(
    private val remoteShelfDataSource: RemoteShelfDataSource,
    private val localShelfDataSource: LocalShelfDataSource
) : ShelfRepository {

//    override suspend fun getBooksByStatusId(statusId: String): Flow<List<>>

}