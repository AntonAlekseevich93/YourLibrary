import database.LocalSynchronizationDataSource
import ktor.RemoteSynchronizationDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val synchronizationModule = DI.Module("synchronizationModule") {
    bind<SynchronizationRepository>() with singleton {
        SynchronizationRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }

    bind<RemoteSynchronizationDataSource>() with provider {
        RemoteSynchronizationDataSource(instance())
    }

    bind<LocalSynchronizationDataSource>() with provider{
        LocalSynchronizationDataSource(instance())
    }
}