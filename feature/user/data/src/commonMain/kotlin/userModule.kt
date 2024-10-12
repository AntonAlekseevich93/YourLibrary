import database.LocalUserDataSource
import ktor.RemoteUserDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val userModule = DI.Module("userModule") {
    bind<UserRepository>() with singleton {
        UserRepositoryImpl(instance(), instance())
    }

    bind<UserInfoProvider>() with singleton {
        UserInfoProviderImpl(instance())
    }

    bind<LocalUserDataSource>() with provider {
        LocalUserDataSource(instance())
    }

    bind<RemoteUserDataSource>() with provider {
        RemoteUserDataSource(instance())
    }
}