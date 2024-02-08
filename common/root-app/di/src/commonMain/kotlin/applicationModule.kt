import database.LocalApplicationDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val applicationModule = DI.Module("applicationModule") {

    bind<ApplicationRepository>() with provider {
        ApplicationRepositoryImpl(instance(), instance())
    }

    bind<LocalApplicationDataSource>() with provider {
        LocalApplicationDataSource(instance())
    }

}