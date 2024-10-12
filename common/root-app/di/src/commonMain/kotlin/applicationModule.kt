import database.LocalApplicationDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val applicationModule = DI.Module("applicationModule") {

    bind<ApplicationRepository>() with provider {
        ApplicationRepositoryImpl(instance(), instance())
    }

    bind<LocalApplicationDataSource>() with provider {
        LocalApplicationDataSource(instance())
    }

    bind<AppConfig>() with singleton {
        AppConfig(instance())
    }

    bind<RemoteConfig>() with singleton {
        RemoteConfig()
    }

    bind<HttpAppClient>() with singleton {
        HttpAppClient(
            instance(),
            instance()
        )
    }

}