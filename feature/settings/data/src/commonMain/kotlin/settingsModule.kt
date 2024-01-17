import database.LocalSettingsDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val settingsModule = DI.Module("settingsModule") {
    bind<SettingsRepository>() with singleton {
        SettingsRepositoryImpl(instance(), instance())
    }

    bind<LocalSettingsDataSource>() with provider {
        LocalSettingsDataSource(instance())
    }
}