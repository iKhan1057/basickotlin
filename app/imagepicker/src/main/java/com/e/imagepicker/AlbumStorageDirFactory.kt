package com.e.imagepicker

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class AlbumStorageDirFactory {
    var albumName = "madlyrad"
    abstract fun getAlbumStorageDir(albumName: String?): File?
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "picture_" /*JPEG_FILE_PREFIX + timeStamp + "_";*/
        val albumF = albumDir
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF)
    }

    @Throws(IOException::class)
    fun createVideoFile(): File {
        // Create an video file name
        val imageFileName = "video_"
        val albumF = albumDir
        return File.createTempFile(imageFileName, VIDEO_FILE_SUFFIX, albumF)
    }

    @Throws(IOException::class)
    fun createAudioFile(): File {
        // Create an Audio file name
        val imageFileName = "audio_"
        val albumF = albumDir
        return File.createTempFile(imageFileName, AUDIO_FILE_SUFFIX, albumF)
    }

    /*boolean isWritable = isExternalStorageWritable();
      boolean isReadable = isExternalStorageReadable();*/
    val albumDir: File?
        get() {
            var storageDir: File? = null
            /*boolean isWritable = isExternalStorageWritable();
          boolean isReadable = isExternalStorageReadable();*/if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                storageDir = getAlbumStorageDir(albumName)
                if (!storageDir!!.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory")
                        return null
                    }
                }
            } else {
                Log.v(TAG, "External storage is not mounted READ/WRITE.")
            }
            return storageDir
        }

    // switch
    @Throws(IOException::class)
    fun setUpPhotoFile(): File {
        return createImageFile()
    }

    @Throws(IOException::class)
    fun setUpVideoFile(): File {
        return createVideoFile()
    }

    @Throws(IOException::class)
    fun setUpAudioFile(): File {
        return createAudioFile()
    } /* Checks if external storage is available for read and write */ /* public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    */

    /* Checks if external storage is available to at least read */ /*
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }*/
    companion object {
        private const val TAG = "AlbumStorageDirFactory"
        private const val JPEG_FILE_SUFFIX = ".jpg"
        private const val VIDEO_FILE_SUFFIX = ".mp4"
        private const val AUDIO_FILE_SUFFIX = ".mp3"
    }
}