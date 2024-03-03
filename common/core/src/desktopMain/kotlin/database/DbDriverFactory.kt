package database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import build_variants.isDebug
import di.PlatformConfiguration
import di.database.DEFAULT_DB_NAME_POSTFIX
import di.database.DEFAULT_DB_NAME_PREFIX
import di.database.PATH_DB_NAME
import sqldelight.com.yourlibrary.database.AppDatabase
import java.io.File
import java.util.Locale

private const val DEBUG_PREFIX = "debug_"

actual class DbDriverFactory actual constructor(private val platformConfiguration: PlatformConfiguration) {
    actual fun createDriver(path: String?, isPathDb: Boolean, dbName: String?): SqlDriver {
        val resultDbName = if (platformConfiguration.buildVariants.isDebug()) {
            if (dbName != null) {
                "$DEBUG_PREFIX$dbName"
            } else {
                "$DEBUG_PREFIX${DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX}"
            }
        } else {
            dbName ?: (DEFAULT_DB_NAME_PREFIX + DEFAULT_DB_NAME_POSTFIX)
        }

        val file = if (path != null && !isPathDb) {
            File(path, resultDbName)
        } else {
            getCacheFolder(PATH_DB_NAME)
        }

//        file.delete()
        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${file.absolutePath}")
        return if (file.exists()) {
            driver // file exist
        } else driver.apply {
            AppDatabase.Schema.create(this) // create a new file
        }
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