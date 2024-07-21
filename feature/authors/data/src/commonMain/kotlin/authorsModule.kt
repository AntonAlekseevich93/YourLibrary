import database.LocalAuthorsDataSource
import ktor.RemoteAuthorsDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val authorsModule = DI.Module("authorsModule") {
    bind<AuthorsRepository>() with singleton {
        AuthorsRepositoryImpl(instance(), instance(), instance())
    }

    bind<LocalAuthorsDataSource>() with provider {
        LocalAuthorsDataSource(instance(), instance())
    }

    bind<RemoteAuthorsDataSource>() with provider {
        RemoteAuthorsDataSource(instance())
    }
}