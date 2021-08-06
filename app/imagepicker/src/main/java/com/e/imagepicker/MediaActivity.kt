package com.e.imagepicker

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.BuildCompat
import java.io.File
import java.io.IOException
import java.util.*


class MediaActivity : AppCompatActivity() {
    private var mImageCaptureUri: Uri? = null
    private var mCurrentPhotoPath: String? = ""
    private var mAlbumStorageDirFactory: AlbumStorageDirFactory? = null
    var handler = Handler()
    var mActvity: Activity? = null
    private val dialog: Dialog? = null
    var isCameraOpen = true
    var isGalleryOpen = true
    var actionOpenFor = 1
    var selectedColor = Color.parseColor("#007AFF")
    var defaultColor = Color.parseColor("#4779B8")
    var playColor = Color.parseColor("#F11514")
    var voiceStoragePath: String? = null
    var btn_cancel: Button? = null
    var btn_done: Button? = null
    var btn_play: Button? = null
    var btn_stop: Button? = null
    var btn_record: Button? = null
    var timer: TextView? = null
    var iv_speakerOn: ImageView? = null
    var tv_record: TextView? = null
    var tv_camera: TextView? = null
    var tv_gallery: TextView? = null
    var tv_cancel: TextView? = null
    var tv_customText: TextView? = null
    private var mRecorder: MediaRecorder? = null
    private var mStartTime: Long = 0
    private val amplitudes = IntArray(100)
    private var i = 0
    private val mHandler = Handler()
    private val mTickExecutor: Runnable = object : Runnable {
        override fun run() {
            tick()
            mHandler.postDelayed(this, 100)
        }
    }
    var mediaPlayer: MediaPlayer? = null
    private val mThreshold = 9
    var ll_record_main: LinearLayout? = null
    private val mPollTask: Runnable? = null
    var FOLDERNAMEFORRECORDING = "voices"
    private var mDrawable: Drawable? = null

    /*
    1 --- for image only
    2 -- for video only
    3-- for image audoi
    4-- for image audoi video
    5-- for audio only

    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        mActvity = this@MediaActivity
        val extras = intent.extras
        if (extras != null) {
            val actionS = extras.getString("action")
            val gallery = extras.getString("gallery")
            val camera = extras.getString("camera")
            actionOpenFor = actionS!!.toInt()
            isCameraOpen = !camera!!.contentEquals("1")
            isGalleryOpen = !gallery!!.contentEquals("1")
        }
        if (actionOpenFor < 0) {
            actionOpenFor = 1
        }
        mAlbumStorageDirFactory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            FroyoAlbumDirFactory()
        } else {
            BaseAlbumDirFactory()
        }
        ll_record_main = findViewById<View>(R.id.ll_record_main) as LinearLayout
        ll_record_main!!.visibility = View.GONE
        openGalleryCameraSelector()
        initview()
    }

    val amplitude: Double
        get() = if (mRecorder != null) mRecorder!!.maxAmplitude / 2700.0 else 0.0

    fun openGalleryCameraSelector() {
        // for action key text
        tv_record = findViewById<View>(R.id.tv_record) as TextView
        tv_camera = findViewById<View>(R.id.tv_camera) as TextView
        tv_gallery = findViewById<View>(R.id.tv_gallery) as TextView
        tv_cancel = findViewById<View>(R.id.tv_cancel) as TextView
        tv_customText = findViewById<View>(R.id.customText) as TextView
        tv_record!!.visibility = View.GONE
        tv_camera!!.visibility = View.GONE
        tv_gallery!!.visibility = View.GONE
        if (actionOpenFor == 5) {
            isCameraOpen = false
            tv_record!!.visibility = View.VISIBLE

            /*tv_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkforrecordingpermission();
                }
            });*/Handler().postDelayed({ checkforrecordingpermission() }, 1000)
        }
        if (isCameraOpen) {
            /*tv_camera.setVisibility(View.VISIBLE);
            tv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFromCamera();
                    // dialog.dismiss();
                }
            });*/
            Handler().postDelayed({ fromCamera }, 1000)
        }
        if (isGalleryOpen) {
            /*tv_gallery.setVisibility(View.VISIBLE);
            tv_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFromGallery();

                }
            });*/
            Handler().postDelayed({ fromGallery }, 1000)
        }
        tv_cancel!!.setOnClickListener { resultSent("", "") }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        resultSent("", "")
    }

    private fun initview() {
        timer = findViewById<View>(R.id.timer) as TextView
        btn_done = findViewById<View>(R.id.btn_done) as Button
        btn_play = findViewById<View>(R.id.btn_play) as Button
        btn_stop = findViewById<View>(R.id.btn_stop) as Button
        btn_record = findViewById<View>(R.id.btn_record) as Button
        iv_speakerOn = findViewById<View>(R.id.iv_speakerOn) as ImageView
        btn_done!!.setOnClickListener {
            if (mCurrentPhotoPath != null || !mCurrentPhotoPath!!.isEmpty()) {
                resultSent(mCurrentPhotoPath, mImageCaptureUri.toString())
            } else {
                Toast.makeText(mActvity, "Current Audio file not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun filenameForRecord() {
        // setUpAudioFile
        val f: File
        try {
            f = mAlbumStorageDirFactory!!.setUpAudioFile()
            mCurrentPhotoPath = f.absolutePath
            mImageCaptureUri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Uri.fromFile(f)
            } else {
                FileProvider.getUriForFile(
                    mActvity!!,
                    "com.gl.provider",
                    f
                )
            }
        } catch (e: IOException) {
            mCurrentPhotoPath = null
            e.printStackTrace()
        }
    }

    private fun openRecorder() {
        if (!isGalleryOpen) {
            ll_record_main!!.visibility = View.VISIBLE
            btn_play!!.isEnabled = false
            btn_done!!.isEnabled = false
            tv_record!!.isClickable = false
            btn_stop!!.isEnabled = false
            tv_record!!.setTextColor(resources.getColor(R.color.black_overlay))
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            filenameForRecord()
            val cfClicked: ColorFilter = PorterDuffColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)
            mDrawable = iv_speakerOn!!.drawable
//            mDrawable.setColorFilter(cfClicked)
            btn_record!!.setOnClickListener { startrecording() }
            btn_stop!!.setOnClickListener { stoprecording() }
            btn_play!!.setOnClickListener { playrecording() }
            tv_customText!!.visibility = View.GONE
        }
    }

    private fun startrecording() {
        val cfClicked: ColorFilter = PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
        btn_record!!.isEnabled = false
        btn_done!!.isEnabled = false
        btn_play!!.isEnabled = false
        btn_stop!!.isEnabled = true
        mDrawable = iv_speakerOn!!.drawable
//        mDrawable.setColorFilter(cfClicked)
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
            mRecorder!!.setAudioEncodingBitRate(48000)
        } else {
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mRecorder!!.setAudioEncodingBitRate(64000)
        }
        mRecorder!!.setAudioSamplingRate(16000)
        mRecorder!!.setOutputFile(mCurrentPhotoPath)
        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
            mStartTime = SystemClock.elapsedRealtime()
            mHandler.postDelayed(mTickExecutor, 100)

            // Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
        } catch (e: IOException) {
            Log.e("Voice Recorder", "prepare() failed " + e.message)
        }
    }

    private fun stoprecording() {
        val cfClicked: ColorFilter = PorterDuffColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)
        mDrawable = iv_speakerOn!!.drawable
//        mDrawable.setColorFilter(cfClicked)
        mRecorder!!.stop()
        mRecorder!!.release()
        mRecorder = null
        mStartTime = 0
        mHandler.removeCallbacks(mTickExecutor)
        // tv_record.setClickable(true);
        // tv_record.setTextColor(getResources().getColor(R.color.colorPrimary));
        btn_record!!.isEnabled = true
        btn_done!!.isEnabled = true
        btn_play!!.isEnabled = true
        btn_stop!!.isEnabled = false
        galleryAddPic()
    }

    private fun playrecording() {
        btn_record!!.isEnabled = false
        btn_stop!!.isEnabled = false
        btn_done!!.isEnabled = false
        val cfClicked: ColorFilter = PorterDuffColorFilter(playColor, PorterDuff.Mode.SRC_IN)
        mDrawable = iv_speakerOn!!.drawable
//        mDrawable.setColorFilter(cfClicked)
        if (mCurrentPhotoPath != null || !mCurrentPhotoPath!!.equals("")) {
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer!!.setDataSource(mCurrentPhotoPath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mediaPlayer!!.start()
            btn_play!!.isEnabled = false
            mediaPlayer!!.setOnCompletionListener {
                btn_play!!.isEnabled = true
                btn_done!!.isEnabled = true
                btn_record!!.isEnabled = true
                //btn_stop.setEnabled(true);
                val cfClicked: ColorFilter =
                    PorterDuffColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)
                mDrawable = iv_speakerOn!!.drawable
//                mDrawable.setColorFilter(cfClicked)
            }
        } else {
            Toast.makeText(mActvity, "Current Audio file not found to play", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun tick() {
        val time = if (mStartTime < 0) 0 else SystemClock.elapsedRealtime() - mStartTime
        val minutes = (time / 60000).toInt()
        val seconds = (time / 1000).toInt() % 60
        val milliseconds = (time / 100).toInt() % 10
        timer!!.text =
            minutes.toString() + ":" + (if (seconds < 10) "0$seconds" else seconds) + "." + milliseconds
        if (mRecorder != null) {
            amplitudes[i] = mRecorder!!.maxAmplitude
            if (i >= amplitudes.size - 1) {
                i = 0
            } else {
                ++i
            }
        }
    }

    private fun resultSent(getPath: String?, uri: String) {
        val intent = Intent()
        intent.putExtra("get_image_path", getPath)
        intent.putExtra("image_uri", uri)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mCurrentPhotoPath != null) {
            outState.putString("cameraImageUri", mCurrentPhotoPath)
        }
        if (actionOpenFor < 0) {
            outState.putString("action", "1")
        } else {
            outState.putString("action", actionOpenFor.toString())
        }
        if (isCameraOpen) {
            outState.putString("camera", "0")
        } else {
            outState.putString("camera", "1")
        }
        if (isGalleryOpen) {
            outState.putString("gallery", "0")
        } else {
            outState.putString("gallery", "1")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey("cameraImageUri")) {
            mCurrentPhotoPath = savedInstanceState.getString("cameraImageUri")
        }
        if (savedInstanceState.containsKey("action")) {
            actionOpenFor = savedInstanceState.getString("action")!!.toInt()
        }
        if (savedInstanceState.containsKey("camera")) {
            val iscamS = savedInstanceState.getString("camera")
            isCameraOpen = !iscamS!!.contentEquals("1")
        }
        if (savedInstanceState.containsKey("gallery")) {
            val isgals = savedInstanceState.getString("gallery")
            isGalleryOpen = !isgals!!.contentEquals("1")
        }
    }

    val fromGallery: Unit
        get() {
            val hasWriteStoragePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showMessageOKCancel(
                        "You need to allow access to Storage"
                    ) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            mActvity!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_CODE_GALLERY
                        )
                    }
                    return
                }
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_CONTACTS),
                    REQUEST_CODE_GALLERY
                )
                return
            }
            openGallery()
        }

    private fun openGallery() {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (actionOpenFor == 2) {
            galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        } else if (actionOpenFor == 3) {
            galleryIntent = Intent()
            // galleryIntent.setType("image video audio");
            galleryIntent.type = "image/* audio/*"
        } else if (actionOpenFor == 4) {
            galleryIntent = Intent()
            galleryIntent.type = "image/* video/* audio/*"
        } else if (actionOpenFor == 5) {
            galleryIntent = Intent()
            galleryIntent.type = "audio/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(galleryIntent, ACTION_OPEN_GALLERY)
    }

    //get the final image uri
    protected fun getImage(mImageCaptureUri: Uri?) {}

    //get the final image path
    protected fun getImagepath(mCurrentPhotoPath: String?) {}

    //get the final bitmap
    protected fun getImage(bitmap: Bitmap?) {}

    // get the file for the selected image
    protected fun getImagFile(file: File?) {}
    val fromCamera: Unit
        get() {
            insertDummyCameraWrapper()
        }

    fun checkforrecordingpermission() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (!addPermission(
                permissionsList,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) permissionsNeeded.add("WRITE_EXTERNAL_STORAGE")
        if (!addPermission(
                permissionsList,
                Manifest.permission.RECORD_AUDIO
            )
        ) permissionsNeeded.add("RECORD_AUDIO")
        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                // Need Rationale
                var message = "You need to grant access to " + permissionsNeeded[0]
                for (i in 1 until permissionsNeeded.size) message =
                    message + ", " + permissionsNeeded[i]
                showMessageOKCancel(
                    message
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        mActvity!!,
                        permissionsList.toTypedArray(),
                        REQUEST_CODE_RECODER
                    )
                }
                return
            }
            ActivityCompat.requestPermissions(
                this, permissionsList.toTypedArray(),
                REQUEST_CODE_RECODER
            )
            return
        }
        openRecorder()
    }

    private fun insertDummyCameraWrapper() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (!addPermission(
                permissionsList,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) permissionsNeeded.add("WRITE_EXTERNAL_STORAGE")
        if (!addPermission(
                permissionsList,
                Manifest.permission.CAMERA
            )
        ) permissionsNeeded.add("CAMERA")
        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                // Need Rationale
                var message = "You need to grant access to " + permissionsNeeded[0]
                for (i in 1 until permissionsNeeded.size) message =
                    message + ", " + permissionsNeeded[i]
                showMessageOKCancel(
                    message
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        mActvity!!,
                        permissionsList.toTypedArray(),
                        REQUEST_CODE_CAMERA
                    )
                }
                return
            }
            ActivityCompat.requestPermissions(
                this, permissionsList.toTypedArray(),
                REQUEST_CODE_CAMERA
            )
            return
        }
        openCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            run {
                val perms: MutableMap<String, Int> =
                    HashMap()
                // Initial
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                // Fill with results
                for (i in permissions.indices) perms[permissions[i]] = grantResults[i]
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted
                    openCamera()
                } else {
                    // Permission Denied
                    Toast.makeText(mActvity, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else if (requestCode == REQUEST_CODE_RECODER) {
            val perms: MutableMap<String, Int> = HashMap()
            // Initial
            perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED
            // Fill with results
            for (i in permissions.indices) perms[permissions[i]] = grantResults[i]
            // Check for ACCESS_FINE_LOCATION
            if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                && perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED
            ) {
                // All Permissions Granted
                openRecorder()
            } else {
                // Permission Denied
                Toast.makeText(mActvity, "Some Permission is Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Can not", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun openCamera() {
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (actionOpenFor == 2) {
            cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 8)
            cameraIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true)
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 16777216) //---16*1024*1024 = 16 MB
            // cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,2097152); //---2*1024*1024 = 25 MB
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        }
        val f: File
        try {
            if (actionOpenFor == 2) {
                f = mAlbumStorageDirFactory!!.setUpVideoFile()
            } else {
                f = mAlbumStorageDirFactory!!.setUpPhotoFile()
            }
            mCurrentPhotoPath = f.absolutePath
            mImageCaptureUri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Uri.fromFile(f)
            } else {
                FileProvider.getUriForFile(
                    mActvity!!,
                    "com.gl.provider",
                    f
                )
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(cameraIntent, ACTION_OPEN_CAMERA)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            mCurrentPhotoPath = null
        }
    }

    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsList.add(permission)
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) return false
        }
        return true
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(mActvity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ACTION_OPEN_GALLERY -> {
                    mImageCaptureUri = data!!.data
                    resultSent(getPath(mActvity, mImageCaptureUri), mImageCaptureUri.toString())
                }
                ACTION_OPEN_CAMERA -> if (mImageCaptureUri != null) {
                    galleryAddPic()
                    handler.postDelayed({
                        var imagePath: String? = ""
                        // String uripath="";
                        imagePath = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                            // getImagFile(uriToFile(mImageCaptureUri));
                            getImagepath(getPath(mActvity, mImageCaptureUri))
                            getPath(mActvity, mImageCaptureUri)
                            //  uripath= String.valueOf(mImageCaptureUri);
                        } else {
                            mCurrentPhotoPath
                        }
                        resultSent(imagePath, mImageCaptureUri.toString())
                    }, 1000)
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        } else {
            resultSent("", "")
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context: Context?, uri: Uri?): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
        val f = File(mCurrentPhotoPath)
        val contentUri: Uri
        contentUri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Uri.fromFile(f)
        } else {
            FileProvider.getUriForFile(
                mActvity!!,
                "com.gl.provider",
                f
            )
        }
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    fun getDataColumn(uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun uriToBitmap(imageUri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            if (imageUri != null) bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bitmap = resizedBitmap(bitmap)
        return bitmap
    }

    private fun uriToFile(imageUri: Uri?): File {
        return if (imageUri != null) {
            File(getPath(applicationContext, imageUri))
        } else {
            File("")
        }
    }

    //----compress image size (1 mb only)
    private fun resizedBitmap(bmp: Bitmap?): Bitmap? {
        var resizedBitmap: Bitmap?
        val maxSize = 512

        //---if small size image than return originaol image only
        if (sizeOf(bmp) < maxSize) {
            resizedBitmap = bmp
            return resizedBitmap
        }
        try {
            val outWidth: Int
            val outHeight: Int
            val inWidth = bmp!!.width
            val inHeight = bmp.height
            if (inWidth > inHeight) {
                outWidth = maxSize
                outHeight = inHeight * maxSize / inWidth
            } else {
                outHeight = maxSize
                outWidth = inWidth * maxSize / inHeight
            }
            resizedBitmap = Bitmap.createScaledBitmap(bmp, outWidth, outHeight, false)
        } catch (e: Exception) {
            resizedBitmap = bmp
            return resizedBitmap
        }
        return resizedBitmap
    }

    //---check Image size
    protected fun sizeOf(data: Bitmap?): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            data!!.rowBytes * data.height
        } else {
            data!!.byteCount
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 1
        private const val REQUEST_CODE_GALLERY = 2
        private const val REQUEST_CODE_RECODER = 3
        private const val ACTION_OPEN_CAMERA = 101
        private const val ACTION_OPEN_GALLERY = 20
        private const val ACTION_OPEN_RECORDING = 40

        /* for sound record*/ //static final String AB = "abcdefghijklmnopqrstuvwxyz";
        var rnd = Random()
        private const val POLL_INTERVAL = 300
        fun isGooglePhotosUri(uri: Uri?): Boolean {
            return "com.google.android.apps.photos.content" == uri!!.authority
        }

        fun isExternalStorageDocument(uri: Uri?): Boolean {
            return "com.android.externalstorage.documents" == uri!!.authority
        }

        fun isDownloadsDocument(uri: Uri?): Boolean {
            return "com.android.providers.downloads.documents" == uri!!.authority
        }

        fun isMediaDocument(uri: Uri?): Boolean {
            return "com.android.providers.media.documents" == uri!!.authority
        }
    }
}