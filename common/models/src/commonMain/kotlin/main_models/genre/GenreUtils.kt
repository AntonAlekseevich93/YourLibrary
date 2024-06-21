package main_models.genre

object GenreUtils {
    private val genreValues = genreMap.values
    fun getGenreById(genreId: Int): Genre =
        genreMap[genreId] ?: throw Throwable("Genre not exist. GenreUtils")

    fun getAllMainGenres(): List<Genre> = genreValues.filter { it.relates == null }

    fun hasInnerGenre(id: Int): Boolean =
        genreValues.count { it.relates == id } > 0

    fun getAllGenresForSelectedGenre(id: Int): List<Genre> =
        genreValues.filter { it.relates == id }
}