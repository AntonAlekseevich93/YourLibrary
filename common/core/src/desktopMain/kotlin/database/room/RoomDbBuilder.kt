package database.room

import androidx.room.Room
import androidx.room.RoomDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import build_variants.isDebug
import database.DEBUG_PREFIX
import di.PlatformConfiguration
import di.database.DEFAULT_DB_NAME_POSTFIX
import di.database.DEFAULT_DB_NAME_PREFIX
import di.database.PATH_DB_NAME
import java.io.File
import java.util.Locale

actual class RoomDbBuilder actual constructor(val platformConfiguration: PlatformConfiguration) {
    actual fun createRoomBuilder(): RoomDatabase.Builder<AppDatabase> {
//        val dbFile = File(System.getProperty("java.io.tmpdir"), ROOM_DB_NAME)
        val dbName = "yourLibrary32"
        val path = "/Users/antonbelonogov/Downloads/Db/Mydb/"
        val resultDbName = if (platformConfiguration.buildVariants.isDebug()) {
            if (dbName != null) {
                "$DEBUG_PREFIX$dbName"
            } else {
                "$DEBUG_PREFIX${DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX}"
            }
        } else {
            dbName ?: (DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX)
        }

        val file = if (path != null) {
            File(path, resultDbName)
        } else {
            getCacheFolder(PATH_DB_NAME)
        }

//        file.delete()
        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${file.absolutePath}")
         if (file.exists()) {
            driver // file exist
        } else driver.apply {
            sqldelight.com.yourlibrary.database.AppDatabase.Schema.create(this) // create a new file
        }
        println("вот он абслдют = ${file.absolutePath}")
        return Room.databaseBuilder<AppDatabase>(
            name = file.absolutePath,
        )
    }

    private fun getCacheFolder(dir: String): File {
        val osName = System.getProperty("os.name", "generic")
        // macOS
        if (osName.lowercase(Locale.getDefault()).contains("mac")) {
            return File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Caches" + File.separator + dir)
        }

        // Linux
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return File(System.getProperty("user.home") + File.separator + ".cache" + File.separator + dir)
        }

        // Windows
        return if (osName.contains("indows")) {
            File(System.getenv("AppData") + File.separator + dir)
        } else {
            // A reasonable fallback
            return File(System.getProperty("user.home") + File.separator + ".cache" + File.separator + dir)
        }
    }
}