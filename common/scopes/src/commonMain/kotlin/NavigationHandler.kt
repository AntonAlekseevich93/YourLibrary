interface NavigationHandler {
    //todo переделать на events
    fun navigateToSearch()
    fun navigateToSelectorVault(needPopBackStack: Boolean = false)
    fun navigateToBookCreator(popUpToMain: Boolean = false)
    fun goBack()
    fun navigateToMain()
    fun restartWindow()
    fun navigateToBookInfo()

    /**don`t use this fun. Use ApplicationScope**/
    fun navigateToBooksListInfo()
    fun navigateToAuthorsScreen()
    fun navigateToJoinAuthorsScreen()
    fun navigateToSettingsScreen()
    fun navigateToProfile()
    fun navigateToAdminPanel()
    fun closeBookInfoScreen()
    fun closeBooksListInfoScreen()
}