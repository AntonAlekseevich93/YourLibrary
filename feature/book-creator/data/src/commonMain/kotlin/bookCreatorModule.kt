import database.LocalBookCreatorDataSource
import ktor.RemoteBookCreatorDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val bookCreatorModule = DI.Module("bookCreatorModule") {
    bind<BookCreatorRepository>() with singleton {
        BookCreatorRepositoryImpl(instance(), instance(), instance(), instance(), instance())
    }

    bind<LocalBookCreatorDataSource>() with singleton {
        LocalBookCreatorDataSource(instance(), instance())
    }

    bind<RemoteBookCreatorDataSource>() with singleton {
        RemoteBookCreatorDataSource(instance())
    }
}