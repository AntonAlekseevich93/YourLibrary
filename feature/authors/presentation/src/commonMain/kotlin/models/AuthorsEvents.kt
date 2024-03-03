package models

import BaseEvent
import main_models.AuthorVo

sealed class AuthorsEvents : BaseEvent {
    class OpenJoinAuthorsScreen(val author: AuthorVo) : AuthorsEvents()
    class OnSearch(val searchingAuthorName: String, val exceptId: String) : AuthorsEvents()
    data object FinishSearch : AuthorsEvents()

    class AddAuthorToRelates(val originalAuthor: AuthorVo, val modifiedAuthorId: String) :
        AuthorsEvents()

    class RemoveAuthorFromRelates(val originalAuthor: AuthorVo, val modifiedAuthorId: String) :
        AuthorsEvents()

    class SetAuthorAsMain(
        val oldAuthorId: String,
        val newAuthorId: String,
        val newAuthorName: String
    ) : AuthorsEvents()
}