import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import main_models.AuthorVo

class AuthorsInteractor(
    private val repository: AuthorsRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun getAllAuthorsByAlphabet(): Flow<LinkedHashMap<String, MutableList<AuthorVo>>> {
        return repository.getAllMainAuthors().transform { authors ->
            emit(getAuthorsByAlphabet(authors))
        }
    }

    suspend fun getAllAuthorsNotSeparatingSimilarWithExceptionId(
        exceptionAuthorId: String
    ): Flow<LinkedHashMap<String, MutableList<AuthorVo>>> {
        return repository.getAllAuthorsNotSeparatingSimilar().transform { authors ->
            emit(getAuthorsByAlphabet(authors, exceptionAuthorId))
        }
    }

    suspend fun searchAuthorExceptId(
        searchingName: String,
        exceptionAuthorId: String
    ): LinkedHashMap<String, MutableList<AuthorVo>> {
        val result = searchRepository.searchInAuthorsName(searchingName)
        return getAuthorsByAlphabet(result, exceptionAuthorId)
    }

    suspend fun addAuthorToRelates(
        originalAuthorId: String,
        originalAuthorName: String,
        modifiedAuthorId: String,
    ) {
        repository.addAuthorToRelates(
            originalAuthorId = originalAuthorId,
            originalAuthorName = originalAuthorName,
            modifiedAuthorId = modifiedAuthorId,
        )
    }

    suspend fun getMainAuthorById(id: String): AuthorVo? {
        repository.getAuthorByIdWithoutRelates(id)?.let { authorVo ->
            val relates = repository.getAllRelatedAuthors(id)
            return authorVo.apply { relatedAuthors = relates }
        }
        return null
    }

    suspend fun removeAuthorFromRelates(originalAuthorId: String) {
        repository.removeAuthorFromRelates(originalAuthorId = originalAuthorId)
    }

    private fun getAuthorsByAlphabet(
        authors: List<AuthorVo>,
        exceptionAuthorId: String? = null
    ): LinkedHashMap<String, MutableList<AuthorVo>> {
        val list = if (exceptionAuthorId != null) {
            authors
                .asSequence()
                .mapNotNull {
                    it.takeIf { it.id != exceptionAuthorId && it.relatedToAuthorId != exceptionAuthorId }
                }
                .toList()
        } else authors

        val resultMap: LinkedHashMap<String, MutableList<AuthorVo>> = linkedMapOf()
        list.forEach { author ->
            val firstLetter = author.name.first().uppercase()
            val currentList =
                if (resultMap.containsKey(firstLetter)) resultMap[firstLetter]!! else mutableListOf()
            currentList.add(author)
            resultMap[firstLetter] = currentList
        }
        return resultMap
    }

}