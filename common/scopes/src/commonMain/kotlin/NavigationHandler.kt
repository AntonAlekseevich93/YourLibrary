interface NavigationHandler {
    //todo переделать на events
    fun navigateToSearch()
    fun navigateToSelectorVault(needPopBackStack: Boolean = false)
    fun navigateToBookCreator(popUpToMain: Boolean = false)
    fun goBack()
    fun navigateToMain()
    fun restartWindow()
    fun navigateToBookInfo()
    fun navigateToAuthorsScreen()
    fun navigateToJoinAuthorsScreen()
    fun navigateToSettingsScreen()
}