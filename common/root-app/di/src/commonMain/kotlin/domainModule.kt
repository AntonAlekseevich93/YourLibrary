import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val domainModule = DI.Module("domainModule") {
    bind<BookCreatorInteractor>() with provider {
        BookCreatorInteractor(instance(), instance(), instance())
    }

    bind<BookInfoInteractor>() with provider {
        BookInfoInteractor(instance(), instance(), instance(), instance())
    }

    bind<BooksListInfoInteractor>() with provider {
        BooksListInfoInteractor(instance(), instance(), instance(), instance())
    }

    bind<AuthorsInteractor>() with provider {
        AuthorsInteractor(instance(), instance())
    }

    bind<AdminInteractor>() with provider {
        AdminInteractor(instance())
    }

    bind<ApplicationInteractor>() with provider {
        ApplicationInteractor(instance(), instance(), instance(), instance())
    }

    bind<UserInteractor>() with provider {
        UserInteractor(instance(), instance(), instance())
    }

}