import di.Inject
import di.PlatformConfiguration
import di.coreModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton
import platform.Platform

object PlatformSDK {
    fun init(
        configuration: PlatformConfiguration,
        platform: Platform,
    ) {
        val rootComposeModule = DI.Module(
            name = "rootComposeModule",
            init = {
                bind<PlatformConfiguration>() with singleton { configuration }
                bind<ApplicationViewModel>() with singleton { ApplicationViewModel(instance()) }
                bind<Platform>() with singleton { platform }
                bind<FileManager>() with singleton { FileManager() }
            })

        Inject.createDependencies(
            DI {
                importAll(
                    rootComposeModule,
                    coreModule,
                    mainScreenModule,
                    shelfModule,
                    searchModule,
                    bookCreatorModule,
                    bookInfoModule,
                    urlParserModule,
                    settingsModule,
                    viewModelsModule,
                )
            }.direct
        )
    }
}
