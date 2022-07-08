package com.lokile.media_utils

import android.app.Application
import android.media.ExifInterface
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

class ImageFile
    (id: Int, name: String, path: String, dateTaken: Long?) :
    MediaFile(id, name, path, dateTaken) {

    init {
        if (this.dateTaken == null) {
            this.dateTaken = getLastModifiedAsLong(path)
        }
    }

    val insertedDateTime: String? by lazy {
        try {
            ExifInterface(path).getAttribute(ExifInterface.TAG_DATETIME)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override val dateTakenFormated = (dateTaken ?: getLastModifiedAsLong(path)).let {
        if (it != 0L) {
            dateFormat.format(it)
        } else if (insertedDateTime != null) {
            insertedDateTime
        } else {
            ""
        }
    }

    companion object {
        fun loadAll(application: Application): List<ImageFile> {
            return application.queryContentProvider(
                arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN
                ),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) { cur ->
                val imageId = cur.getString(cur.getColumnIndex(MediaStore.Images.Media._ID))
                val path = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA))
                ImageFile(
                    imageId.toInt(),
                    cur.getStringOrNull(cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        .orEmpty(),
                    path,
                    cur.getLongOrNull(cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)),
                )
            }.reversed()
        }
    }

}