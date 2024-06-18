class RemoteConfig {
    private var s3_URL = "https://s3.timeweb.cloud/"
    private var s3_bucket = "13109edd-5c7f9725-5507-4e66-8f9a-c54f7bf66003"
    private var s3_FULL_PREFIX = "$s3_URL$s3_bucket/"
    private val S3_BOOK_IMAGES_PATH_OLD = "books/images/"
    private val S3_BOOK_IMAGES_PATH_PREFIX = "books/"
    private val S3_IMAGE_URL_PREFIX = "$s3_FULL_PREFIX$S3_BOOK_IMAGES_PATH_OLD"

    fun getImageUrl(imageName: String?, imageFolderId: Int?, bookServerId: Int?): String? {
        if (imageName == null || bookServerId == null) return null
        return if (imageFolderId != null) {
            val folder = imageFolderId.toString()
            "$s3_FULL_PREFIX$S3_BOOK_IMAGES_PATH_PREFIX$folder/$imageName"
        } else {
            "$s3_FULL_PREFIX$S3_BOOK_IMAGES_PATH_OLD$imageName"
        }
    }

}