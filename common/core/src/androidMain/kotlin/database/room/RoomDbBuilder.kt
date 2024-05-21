package database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import di.PlatformConfiguration

actual class RoomDbBuilder actual constructor(private val platformConfiguration: PlatformConfiguration) {
  actual  fun createRoomBuilder(): RoomDatabase.Builder<AppDatabase> {
        val appContext = platformConfiguration.androidContext
        val dbFile = appContext.getDatabasePath(ROOM_DB_NAME)
        return Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}