package database.room

import androidx.sqlite.SQLiteDriver
expect class SqlDriverProvider(){
    fun provideDriver(): SQLiteDriver
}