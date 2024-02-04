interface NavigationHandler {
    fun navigateToSearch()
    fun navigateToSelectorVault(needPopBackStack: Boolean = false)
    fun navigateToBookCreator(popUpToMain: Boolean = false)
    fun goBack()
    fun navigateToMain()
    fun restartWindow()
    fun navigateToBookInfo()
}