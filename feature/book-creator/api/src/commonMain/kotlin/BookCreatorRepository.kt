import main_models.BookVo

interface BookCreatorRepository {
    suspend fun createBook(book: BookVo)
}