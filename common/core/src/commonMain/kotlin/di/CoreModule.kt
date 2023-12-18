package di

import di.database.databaseModule
import di.ktor.ktorModule
import org.kodein.di.DI

val coreModule = DI.Module("coreModule") {
    importAll(
        databaseModule,
        ktorModule,
    )
}