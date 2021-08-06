package com.e.imagepicker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream

object BitmapUtils {
    private const val TAG = "BitmapUtils"
    fun decodeSampledBitmapFromFile(filePath: String, width: Int, height: Int): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var stream: BufferedInputStream? = null
        if (filePath.contains("http://") || filePath.contains("https://")) {
            val build: OkHttpClient = OkHttpClient.Builder().build()
            val request: Request = Request.Builder().url(filePath).build()
            try {
                val response: Response = build.newCall(request).execute()
                val `is`: InputStream = response.body()!!.byteStream()
                stream = BufferedInputStream(`is`)
                BitmapFactory.decodeStream(stream, null, options)
                stream.reset()
            } catch (e: IOException) {
                e.printStackTrace()
                BitmapFactory.decodeFile(filePath, options)
            }
        } else {
            BitmapFactory.decodeFile(filePath, options)
        }


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return if (stream != null) {
            BitmapFactory.decodeStream(stream, null, options)
        } else try {
            rotateImageIfRequired(BitmapFactory.decodeFile(filePath, options), filePath)
        } catch (e: IOException) {
            BitmapFactory.decodeFile(filePath, options)
        }
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, filePath: String): Bitmap {
        val ei = ExifInterface(filePath)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, newWidth: Int, newHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > newHeight || width > newWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > newHeight && halfWidth / inSampleSize > newWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
