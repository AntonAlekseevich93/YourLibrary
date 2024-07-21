import database.LocalAdminDataSource
import ktor.RemoteAdminDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val adminModule = DI.Module("adminModule") {
    bind<AdminRepository>() with singleton {
        AdminRepositoryImpl(instance(), instance(), instance(), instance())
    }

    bind<LocalAdminDataSource>() with provider {
        LocalAdminDataSource(instance())
    }

    bind<RemoteAdminDataSource>() with provider {
        RemoteAdminDataSource(instance(), instance())
    }
}