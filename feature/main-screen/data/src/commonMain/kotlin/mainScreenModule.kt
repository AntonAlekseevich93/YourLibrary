import database.LocalMainScreenDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val mainScreenModule = DI.Module("mainScreenModule") {
    bind<MainScreenRepository>() with singleton {
        MainScreenRepositoryImpl(instance())
    }

    bind<LocalMainScreenDataSource>() with provider {
        LocalMainScreenDataSource(instance())
    }
}