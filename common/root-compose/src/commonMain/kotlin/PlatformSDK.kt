import di.Inject
import di.PlatformConfiguration
import di.coreModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton

object PlatformSDK {
    fun init(
        configuration: PlatformConfiguration
    ) {
        val rootComposeModule = DI.Module(
            name = "rootComposeModule",
            init = {
                bind<PlatformConfiguration>() with singleton { configuration }
                bind<ApplicationViewModel>() with singleton { ApplicationViewModel() }
            })

        Inject.createDependencies(
            DI {
                importAll(
                    rootComposeModule,
                    coreModule,
                    shelfModule,
                    searchModule,
                    bookModule,
                    viewModelsModule,
                )
            }.direct
        )
    }
}
