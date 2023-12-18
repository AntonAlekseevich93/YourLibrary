import database.LocalBookDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val bookModule = DI.Module("bookModule") {
    bind<BookRepository>() with singleton {
        BookRepositoryImpl(instance())
    }

    bind<LocalBookDataSource>() with provider {
        LocalBookDataSource(instance())
    }
}