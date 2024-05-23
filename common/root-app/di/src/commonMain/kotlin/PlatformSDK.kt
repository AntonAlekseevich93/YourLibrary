import di.Inject
import di.PlatformConfiguration
import di.coreModule
import models.SettingsDataProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton
import platform.Platform
import platform.PlatformInfoData

object PlatformSDK {
    fun init(
        configuration: PlatformConfiguration,
        platformInfo: PlatformInfoData,
        platform: Platform,
        navigationHandler: NavigationHandler,
        tooltipHandler: TooltipHandler,
    ) {
        val rootComposeModule = DI.Module(
            name = "rootComposeModule",
            init = {
                bind<NavigationHandler>() with singleton { navigationHandler }
                bind<TooltipHandler>() with singleton { tooltipHandler }
                bind<PlatformConfiguration>() with singleton { configuration }
                bind<ApplicationViewModel>() with singleton {
                    ApplicationViewModel(instance(), instance(), instance(), instance(), instance())
                }
                bind<Platform>() with singleton { platform }
                bind<FileManager>() with singleton { FileManager() }
                bind<PlatformInfoData>() with singleton { platformInfo }
                bind<ApplicationScope>() with singleton {
                    Inject.instance<ApplicationViewModel>()
                }
                bind<DrawerScope>() with singleton {
                    Inject.instance<ApplicationViewModel>()
                }
                bind<MainScreenScope<BaseEvent>>() with singleton {
                    Inject.instance<MainScreenViewModel>()
                }
                bind<SettingsDataProvider>() with singleton {
                    Inject.instance<SettingsViewModel>()
                }
            })
        Inject.createDependencies(
            DI {
                importAll(
                    rootComposeModule,
                    applicationModule,
                    coreModule,
                    mainScreenModule,
                    shelfModule,
                    searchModule,
                    bookCreatorModule,
                    bookInfoModule,
                    urlParserModule,
                    settingsModule,
                    authorsModule,
                    adminModule,
                    userModule,
                    domainModule,
                    viewModelsModule,
                )
            }.direct
        )
    }
}
