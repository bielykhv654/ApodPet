package utils

import network.dto.NasaPhotoDto
import database.NasaPhotoEntity

data class NasaPhotoUiModel(
    val id: String,
    val title: String,
    val explanation: String,
    val url: String,
    val hdurl: String?,
    val date: String,
    val mediaType: String?,
    val copyright: String?,
    val isSaved: Boolean = false
)

fun NasaPhotoDto.toUiModel(isSaved: Boolean = false): NasaPhotoUiModel {
    val photoId = date ?: url ?: title ?: "unknown"
    return NasaPhotoUiModel(
        id = photoId,
        title = title ?: "No Title",
        explanation = explanation ?: "",
        url = url ?: "",
        hdurl = hdurl,
        date = date ?: "",
        mediaType = mediaType,
        copyright = copyright,
        isSaved = isSaved
    )
}

fun NasaPhotoUiModel.toDto(): NasaPhotoDto {
    return NasaPhotoDto(
        date = date,
        explanation = explanation,
        hdurl = hdurl,
        mediaType = mediaType,
        title = title,
        url = url,
        copyright = copyright
    )
}

fun NasaPhotoEntity.toUiModel(): NasaPhotoUiModel {
    return NasaPhotoUiModel(
        id = id,
        title = title,
        explanation = explanation,
        url = url,
        hdurl = hdurl,
        date = date,
        mediaType = mediaType,
        copyright = copyright,
        isSaved = true
    )
}

fun NasaPhotoUiModel.toEntity(savedAt: Long = 0L): NasaPhotoEntity {
    return NasaPhotoEntity(
        id = id,
        title = title,
        explanation = explanation,
        url = url,
        hdurl = hdurl,
        date = date,
        mediaType = mediaType,
        copyright = copyright,
        savedAt = savedAt
    )
}
