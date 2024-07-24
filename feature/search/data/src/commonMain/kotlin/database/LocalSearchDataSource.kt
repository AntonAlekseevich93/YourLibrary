package database

import database.room.RoomMainDataSource

class LocalSearchDataSource(
    roomDb: RoomMainDataSource,
) {
    private val booksDao = roomDb.booksDao

    suspend fun searchInBooks(bookName: String) =
        booksDao.searchBookByName(bookName.uppercase())

}