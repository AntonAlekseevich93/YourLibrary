import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val viewModelsModule = DI.Module("viewModelsModule") {
    bind<ShelfViewModel>() with singleton {
        ShelfViewModel(instance())
    }
    bind<SearchViewModel>() with singleton {
        SearchViewModel(instance())
    }
    bind<BookViewModel>() with singleton {
        BookViewModel(instance())
    }
}