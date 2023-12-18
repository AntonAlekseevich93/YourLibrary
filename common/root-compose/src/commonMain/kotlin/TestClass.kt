import di.Inject

class TestClass {
    val repository = ShelfRepositoryImpl(Inject.instance(), Inject.instance())
    fun hello(): String {
        val rest = repository.test()
        println(rest)
        return rest
    }
}