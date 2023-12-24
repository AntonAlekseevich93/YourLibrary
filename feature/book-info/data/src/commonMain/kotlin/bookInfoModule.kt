import database.LocalBookInfoDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val bookInfoModule = DI.Module("bookInfoModule") {
    bind<BookInfoRepository>() with singleton {
        BookInfoRepositoryImpl(instance())
    }

    bind<LocalBookInfoDataSource>() with provider {
        LocalBookInfoDataSource(instance())
    }
}