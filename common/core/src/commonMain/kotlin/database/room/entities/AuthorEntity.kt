package database.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import main_models.AuthorVo

@Entity
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true) @SerialName("localId") val localId: Long? = null,
    @SerialName("id") val id: String,
    @SerialName("serverId") val serverId: Int?,
    @SerialName("name") val name: String,
    @SerialName("uppercaseName") val uppercaseName: String,
    @SerialName("timestampOfCreating") val timestampOfCreating: Long,
    @SerialName("timestampOfUpdating") val timestampOfUpdating: Long,
    @SerialName("isCreatedByUser") val isCreatedByUser: Boolean,
    @SerialName("userId") val userId: Int,
)

fun AuthorVo.toLocalDto(userId: Int) = AuthorEntity(
    localId = localId,
    id = id,
    serverId = serverId,
    name = name,
    uppercaseName = uppercaseName,
    timestampOfCreating = timestampOfCreating,
    timestampOfUpdating = timestampOfUpdating,
    isCreatedByUser = isCreatedByUser,
    userId = userId,
)

fun AuthorEntity.toVo(): AuthorVo =
    AuthorVo(
        serverId = serverId,
        localId = localId,
        id = id,
        name = name,
        uppercaseName = uppercaseName,
        timestampOfCreating = timestampOfCreating,
        timestampOfUpdating = timestampOfUpdating,
        isCreatedByUser = isCreatedByUser,
    )