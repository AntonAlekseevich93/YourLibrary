package di

object ViewModelStackStore {
    val viewModelList = mutableListOf<Any>()
    inline fun <reified T> getPreviousViewModelOrNull(): T? {
        viewModelList.removeLastOrNull()
        val item = viewModelList.lastOrNull()
        return if (item is T) {
            item
        } else {
            null
        }
    }

    inline fun <reified T> createViewModel(): T {
        val newViewModel = Inject.instance<T>()
        viewModelList.add(newViewModel as Any)
        return newViewModel
    }

    fun clearViewModelStack() {
        viewModelList.clear()
    }
}