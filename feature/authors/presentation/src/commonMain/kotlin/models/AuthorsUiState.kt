package models

import base.BaseUIState
import main_models.AuthorVo

data class AuthorsUiState(
    val authorByAlphabet: LinkedHashMap<String, MutableList<AuthorVo>> = LinkedHashMap()
) : BaseUIState