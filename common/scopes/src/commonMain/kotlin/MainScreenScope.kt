import main_models.BookItemVo

interface MainScreenScope<BaseEvent> : BaseEventScope<BaseEvent> {
    fun checkIfNeedUpdateBookItem(oldItem: BookItemVo, newItem: BookItemVo)
    fun changedReadingStatus(oldStatusId: String, bookId: String)
}