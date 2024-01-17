import java.io.File

class FileManagerResponse(
    val isSuccess: Boolean = false,
    val fileIsNotExist: Boolean = false,
    val jsonString: String? = null,
    val file: File? = null,
    val throwable: Throwable? = null
)