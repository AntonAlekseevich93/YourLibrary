import database.LocalBooksListInfoDataSource
import ktor.RemoteBooksListInfoDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val booksListInfoModule = DI.Module("booksListInfoModule") {
    bind<BooksListInfoRepository>() with singleton {
        BooksListInfoRepositoryImpl(instance(), instance(), instance(), instance(), instance())
    }

    bind<LocalBooksListInfoDataSource>() with provider {
        LocalBooksListInfoDataSource(instance(), instance())
    }

    bind<RemoteBooksListInfoDataSource>() with provider {
        RemoteBooksListInfoDataSource(instance())
    }
}