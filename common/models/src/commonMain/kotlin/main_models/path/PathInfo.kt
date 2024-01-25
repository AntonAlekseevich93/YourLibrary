package main_models.path

/** need update DatabaseUtils if change values **/
class PathInfoDto(
    val id: Int?,
    val path: String?,
    val libraryName: String?,
    val dbName: String?,
    val isSelected: Int?,
)

class PathInfoVo(
    val id: Int = -1,
    val path: String = "",
    val libraryName: String = "",
    val dbName: String = "",
    val isSelected: Boolean = false
)

fun PathInfoDto.toVo(): PathInfoVo? {
    return if (id == null || path == null || libraryName == null || dbName == null || isSelected == null) null
    else PathInfoVo(
        id = id,
        path = path,
        libraryName = libraryName,
        dbName = dbName,
        isSelected = isSelected == 0
    )
}