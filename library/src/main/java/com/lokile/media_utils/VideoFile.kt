package com.lokile.media_utils

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import java.io.File

class VideoFile(
    id: Int,
    name: String,
    path: String,
    dateTaken: Long?,
    var duration: Long
) :
    MediaFile(id, name, path, dateTaken) {

    companion object {
        fun loadAll(application: Application): List<VideoFile> {
            return application.queryContentProvider(
                arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DATE_TAKEN
                ),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ) { cur ->
                val path = cur.getString(cur.getColumnIndex(MediaStore.Video.Media.DATA))
                val file = File(path)

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(application, Uri.fromFile(file))
                val time =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?: "0"

                retriever.release()
                VideoFile(
                    cur.getInt(cur.getColumnIndex(MediaStore.Video.Media._ID)),
                    file.name,
                    path,
                    cur.getLongOrNull(cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)),
                    time.toLong()
                )
            }.reversed()
        }
    }
}