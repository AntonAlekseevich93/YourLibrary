package database.room

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.AndroidSQLiteDriver

actual class SqlDriverProvider{
  actual  fun provideDriver(): SQLiteDriver = AndroidSQLiteDriver()
}