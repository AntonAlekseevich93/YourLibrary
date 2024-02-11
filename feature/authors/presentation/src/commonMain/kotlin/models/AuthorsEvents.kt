package models

import BaseEvent
import main_models.AuthorVo

sealed class AuthorsEvents : BaseEvent {
    class OpenJoinAuthorsScreen(val author: AuthorVo) : AuthorsEvents()
    class OnSearch(val searchingAuthorName: String, val exceptId: String) : AuthorsEvents()
    data object FinishSearch : AuthorsEvents()

    class AddAuthorToRelates(val mainAuthor: AuthorVo, val selectedAuthorId: String) :
        AuthorsEvents()

    class RemoveAuthorFromRelates(val mainAuthor: AuthorVo, val selectedAuthorId: String) :
        AuthorsEvents()
}