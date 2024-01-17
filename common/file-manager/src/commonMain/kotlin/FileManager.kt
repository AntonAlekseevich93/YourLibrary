import okio.FileSystem
import okio.Path.Companion.toPath
import java.io.File

class FileManager {
    suspend fun createFile(
        path: String,
        jsonString: String,
        fileName: String,
    ): FileManagerResponse {
        val rawPath = "$path$fileName"
        val resultPath = rawPath.toPath()
        return try {
            val needCreateFile = !File(rawPath, fileName).exists()
            FileSystem.SYSTEM.write(resultPath, needCreateFile, writerAction = {})
            FileSystem.SYSTEM.write(resultPath) {
                writeUtf8(jsonString)
            }
            FileManagerResponse(
                isSuccess = true
            )
        } catch (throwable: Throwable) {
            //todo log
            FileManagerResponse(
                throwable = throwable
            )
        }
    }

    suspend fun getFileAsJson(
        path: String,
        fileName: String,
    ): FileManagerResponse {
        val rawPath = "$path$fileName"
        val resultPath = rawPath.toPath()
        val file = File(rawPath)
        if (!file.exists()) return FileManagerResponse(fileIsNotExist = true)
        return try {
            val json = FileSystem.SYSTEM.read(resultPath) {
                readUtf8()
            }
            FileManagerResponse(
                isSuccess = true,
                jsonString = json,
            )
        } catch (throwable: Throwable) {
            //todo log
            FileManagerResponse(
                throwable = throwable
            )
        }
    }

    suspend fun replaceTextInFile(
        path: String,
        fileName: String,
        oldText: String,
        newText: String
    ): Boolean {
        val rawPath = "$path$fileName"
        val resultPath = rawPath.toPath()
        val file = File(rawPath)

        if (file.exists()) {
            try {
                val jsonString = FileSystem.SYSTEM.read(resultPath) {
                    readUtf8()
                }

                val updatedContent = jsonString.replace(oldText, newText)

                FileSystem.SYSTEM.write(resultPath) {
                    writeUtf8(updatedContent)
                }
                return true
            } catch (throwable: Throwable) {
                //todo log
                return false
            }
        }
        return false
    }
}