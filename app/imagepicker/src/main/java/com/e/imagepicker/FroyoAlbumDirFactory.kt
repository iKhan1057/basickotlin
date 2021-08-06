package com.e.imagepicker


import android.os.Environment
import java.io.File

class FroyoAlbumDirFactory : AlbumStorageDirFactory() {

    override fun getAlbumStorageDir(albumName: String?): File? {
        return File(
            Environment.getExternalStoragePublicDirectory(
                Environment.MEDIA_MOUNTED
            ),
            albumName
        )
    }
}