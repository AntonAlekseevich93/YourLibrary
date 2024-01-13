package di.database

import database.DbDriverFactory
import database.SqlDelightDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val databaseModule = DI.Module("databaseModule") {
    bind<DbDriverFactory>() with singleton {
        DbDriverFactory(instance())
    }

    bind<SqlDelightDataSource>() with singleton {
        SqlDelightDataSource(instance())
    }
}