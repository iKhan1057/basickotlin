package com.e.imagepicker

import android.os.Environment
import java.io.File


class BaseAlbumDirFactory() : AlbumStorageDirFactory() {
    override fun getAlbumStorageDir(albumName: String?): File? {
        return File(
            Environment.getExternalStorageDirectory()
                .toString() + CAMERA_DIR
                    + albumName
        )
    }

    companion object {
        // Standard storage location for digital camera files
        private val CAMERA_DIR = "/dcim/"
    }
}