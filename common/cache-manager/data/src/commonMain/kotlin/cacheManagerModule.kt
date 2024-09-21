import database.CacheManagerDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val cacheManagerModule = DI.Module("cacheManagerModule") {
    bind<CacheManagerRepository>() with singleton {
        CacheManagerRepositoryImpl(instance(), instance(), instance())
    }

    bind<CacheManagerDataSource>() with provider {
        CacheManagerDataSource(instance(), instance())
    }
}