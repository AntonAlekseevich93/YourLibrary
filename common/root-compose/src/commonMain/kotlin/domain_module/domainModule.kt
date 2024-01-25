package domain_module

import BookCreatorInteractor
import BookInfoInteractor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val domainModule = DI.Module("domainModule") {
    bind<BookCreatorInteractor>() with provider {
        BookCreatorInteractor(instance(), instance(), instance())
    }

    bind<BookInfoInteractor>() with provider {
        BookInfoInteractor(instance(), instance(), instance())
    }
}