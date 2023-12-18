import database.LocalShelfDataSource
import ktor.RemoteShelfDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val shelfModule = DI.Module("shelfModule") {
    bind<ShelfRepository>() with singleton {
        ShelfRepositoryImpl(instance(), instance())
    }

    bind<LocalShelfDataSource>() with provider {
        LocalShelfDataSource(instance())
    }

    bind<RemoteShelfDataSource>() with provider {
        RemoteShelfDataSource(instance())
    }
}