class RemoteConfig {
    var s3_URL = "https://s3.timeweb.cloud/"
    var s3_bucket = "13109edd-5c7f9725-5507-4e66-8f9a-c54f7bf66003"
    var s3_FULL_PREFIX = "$s3_URL$s3_bucket/"
    val S3_BOOK_IMAGES_PATH = "books/images/"
    val S3_IMAGE_URL_PREFIX = "$s3_FULL_PREFIX$S3_BOOK_IMAGES_PATH"

    fun getImageUrl(imageName: String): String {
        return "$s3_FULL_PREFIX$S3_BOOK_IMAGES_PATH$imageName"
    }

}