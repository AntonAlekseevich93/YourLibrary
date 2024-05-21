package database.room

import androidx.room.RoomDatabase
import di.PlatformConfiguration

expect class RoomDbBuilder constructor(platformConfiguration: PlatformConfiguration) {
    fun createRoomBuilder(): RoomDatabase.Builder<AppDatabase>
}