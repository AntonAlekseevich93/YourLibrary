package database

import database.room.RoomMainDataSource
import platform.PlatformInfoData

class CacheManagerDataSource(
    private val platformInfo: PlatformInfoData,
    roomDb: RoomMainDataSource,
) {
    private val booksByAuthorDao = roomDb.cacheBooksByAuthorDao


}