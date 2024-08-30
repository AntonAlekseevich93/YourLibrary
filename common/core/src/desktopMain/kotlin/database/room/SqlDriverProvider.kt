package database.room

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual class SqlDriverProvider {
    actual fun provideDriver(): SQLiteDriver = BundledSQLiteDriver()
}