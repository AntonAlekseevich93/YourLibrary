interface BookInfoRepository {
    suspend fun getAllRemoteBooksWithAuthorsByTimestamps(): Boolean
}