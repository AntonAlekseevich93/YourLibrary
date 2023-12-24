import database.LocalBookCreatorDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val bookCreatorModule = DI.Module("bookCreatorModule") {
    bind<BookCreatorRepository>() with singleton {
        BookCreatorRepositoryImpl(instance())
    }

    bind<LocalBookCreatorDataSource>() with provider {
        LocalBookCreatorDataSource(instance())
    }
}