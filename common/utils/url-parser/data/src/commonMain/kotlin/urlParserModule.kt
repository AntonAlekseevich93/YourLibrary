import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val urlParserModule = DI.Module("urlParserModule") {
    bind<UrlParserInteractor>() with singleton {
        UrlParserInteractorImpl()
    }
}