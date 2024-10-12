import database.LocalSearchDataSource
import ktor.RemoteSearchDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val searchModule = DI.Module("searchModule") {
    bind<SearchRepository>() with singleton {
        SearchRepositoryImpl(instance(), instance(), instance(), instance(), instance(), instance())
    }

    bind<LocalSearchDataSource>() with provider {
        LocalSearchDataSource(instance())
    }

    bind<RemoteSearchDataSource>() with provider {
        RemoteSearchDataSource(instance())
    }
}