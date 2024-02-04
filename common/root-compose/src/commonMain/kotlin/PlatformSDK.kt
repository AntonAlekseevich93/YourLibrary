import di.Inject
import di.PlatformConfiguration
import di.coreModule
import domain_module.domainModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton
import platform.Platform

object PlatformSDK {
    fun init(
        configuration: PlatformConfiguration,
        platformInfo: PlatformInfo,
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
                    ApplicationViewModel(instance(), instance(), instance())
                }
                bind<Platform>() with singleton { platform }
                bind<FileManager>() with singleton { FileManager() }
                bind<PlatformInfo>() with singleton { platformInfo }
                bind<ApplicationScope>() with singleton {
                    ApplicationViewModel(instance(), instance(), instance())
                }
                bind<DrawerScope>() with singleton {
                    ApplicationViewModel(instance(), instance(), instance())
                }
                bind<MainScreenScope<BaseEvent>>() with singleton {
                    MainScreenViewModel(instance(), instance(), instance(), instance())
                }
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
                    authorsModule,
                    domainModule,
                    viewModelsModule,
                )
            }.direct
        )
    }
}
