import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

val viewModelsModule = DI.Module("viewModelsModule") {
    bind<MainScreenViewModel>() with singleton {
        MainScreenViewModel(instance(), instance(), instance(), instance(), instance())
    }
    bind<ShelfViewModel>() with singleton {
        ShelfViewModel(instance(), instance(), instance())
    }
    bind<SearchViewModel>() with singleton {
        SearchViewModel(instance())
    }
    bind<BookInfoViewModel>() with factory {
        BookInfoViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
    bind<BookCreatorViewModel>() with singleton {
        BookCreatorViewModel(instance(), instance(), instance(), instance())
    }
    bind<SettingsViewModel>() with singleton {
        SettingsViewModel(instance(), instance(), instance())
    }
    bind<AuthorsViewModel>() with singleton {
        AuthorsViewModel(instance(), instance(), instance(), instance(), instance())
    }
    bind<UserViewModel>() with singleton {
        UserViewModel(instance(), instance(), instance(), instance(), instance(), instance())
    }

    bind<AdminViewModel>() with singleton {
        AdminViewModel(instance(), instance(), instance(), instance(), instance(), instance())
    }
    bind<BooksListInfoViewModel>() with singleton {
        BooksListInfoViewModel(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
}