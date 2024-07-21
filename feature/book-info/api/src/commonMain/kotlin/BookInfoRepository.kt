interface BookInfoRepository {
    suspend fun synchronizeBooksWithAuthors(): Boolean
}