package di

object ViewModelStackStore {
    val viewModelList = mutableListOf<Any>()
    fun getPreviousViewModelOrNull(): Any? {
        viewModelList.removeLastOrNull()
        return viewModelList.lastOrNull()
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