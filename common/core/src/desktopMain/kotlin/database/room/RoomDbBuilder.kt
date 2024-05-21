package database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import di.PlatformConfiguration
import java.io.File

actual class RoomDbBuilder actual constructor(val platformConfiguration: PlatformConfiguration) {
    actual fun createRoomBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), ROOM_DB_NAME)
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}