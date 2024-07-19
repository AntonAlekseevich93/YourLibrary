import database.LocalBookInfoDataSource
import ktor.RemoteBookInfoDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val bookInfoModule = DI.Module("bookInfoModule") {
    bind<BookInfoRepository>() with singleton {
        BookInfoRepositoryImpl(instance(), instance(), instance(), instance())
    }

    bind<LocalBookInfoDataSource>() with provider {
        LocalBookInfoDataSource(instance(), instance())
    }

    bind<RemoteBookInfoDataSource>() with provider {
        RemoteBookInfoDataSource(instance())
    }
}