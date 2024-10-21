import database.LocalReviewAndRatingDataSource
import ktor.RemoteReviewAndRatingDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val reviewAndRatingModule = DI.Module("reviewAndRatingModule") {
    bind<ReviewAndRatingRepository>() with singleton {
        ReviewAndRatingRepositoryImpl(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }

    bind<LocalReviewAndRatingDataSource>() with provider {
        LocalReviewAndRatingDataSource(instance(), instance())
    }

    bind<RemoteReviewAndRatingDataSource>() with provider {
        RemoteReviewAndRatingDataSource(instance())
    }
}