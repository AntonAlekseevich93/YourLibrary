import main_models.AuthorVo

class AuthorsInteractor(
    private val repository: AuthorsRepository,
) {
    suspend fun getAllAuthorsByAlphabet(): LinkedHashMap<String, MutableList<AuthorVo>> {
        val resultMap: LinkedHashMap<String, MutableList<AuthorVo>> = linkedMapOf()
        repository.getAllAuthors().forEach { author ->
            val firstLetter = author.name.first().uppercase()
            val currentList =
                if (resultMap.containsKey(firstLetter)) resultMap[firstLetter]!! else mutableListOf()
            currentList.add(author)
            resultMap[firstLetter] = currentList
        }
        return resultMap
    }

}