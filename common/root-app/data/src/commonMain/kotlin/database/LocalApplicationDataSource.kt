package database

class LocalApplicationDataSource(
    private val db: SqlDelightDataSource
) {
//    suspend fun getAllBooks(): Flow<List<BookItemLocalDto>> =
//        db.appQuery.getAllBooks().asFlow().mapToList(Dispatchers.IO)
//            .map { list -> list.map { item -> item.map() } }

}