import database.LocalServiceDevelopmentDataSource
import ktor.RemoteServiceDevelopmentDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val serviceDevelopmentModule = DI.Module("serviceDevelopmentModule") {
    bind<ServiceDevelopmentRepository>() with singleton {
        ServiceDevelopmentRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }

    bind<LocalServiceDevelopmentDataSource>() with provider {
        LocalServiceDevelopmentDataSource(instance(), instance())
    }

    bind<RemoteServiceDevelopmentDataSource>() with provider {
        RemoteServiceDevelopmentDataSource(instance())
    }
}