import main_models.TooltipItem

interface DrawerScope {
    fun openLeftDrawerOrClose()
    fun openRightDrawerOrClose()
    fun setTooltip(tooltip: TooltipItem)
}