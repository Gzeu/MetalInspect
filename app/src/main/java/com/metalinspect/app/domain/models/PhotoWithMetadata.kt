package com.metalinspect.app.domain.models

import com.metalinspect.app.data.database.entities.InspectionPhoto
import com.metalinspect.app.data.database.entities.DefectRecord

data class PhotoWithMetadata(
    val photo: InspectionPhoto,
    val relatedDefect: DefectRecord? = null,
    val fileExists: Boolean = true,
    val thumbnailPath: String? = null
) {
    val isLinkedToDefect: Boolean get() = relatedDefect != null
    val hasCaption: Boolean get() = !photo.caption.isNullOrBlank()
    val fileSizeKB: Long get() = photo.fileSize / 1024
    val aspectRatio: Float get() = photo.imageWidth.toFloat() / photo.imageHeight.toFloat()
}