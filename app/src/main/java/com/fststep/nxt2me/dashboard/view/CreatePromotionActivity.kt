package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.view.ImagePickerDialog
import com.fststep.nxt2me.databinding.ActivityCreatePromotionBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreatePromotionActivity: AppCompatActivity() {

    private lateinit var mBinding:ActivityCreatePromotionBinding
    private lateinit var currentPhotoPath: String
    private var selectedImageBitmap: Bitmap? = null
    private var editModeImageSelectionCallback: ((bitmap: Bitmap) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_promotion)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.clCamera.setOnClickListener {
            showImagePicker(true, ::setSelectedImage)
        }

        mBinding.ivClearImage.setOnClickListener {
            clearSelectedImage()
        }

        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                mBinding.tvTotalWords.text = s.length.toString()
            }
        })

        mBinding.tvPreview.setOnClickListener {
            mBinding.clPreview.visibility = View.VISIBLE
            mBinding.ivImage.setImageDrawable(mBinding.ivSelectedImage.drawable)
            mBinding.tvName.text = mBinding.etPromotionName.text.toString()
            mBinding.tvContent.text = mBinding.etContent.text.toString()
        }
    }

    private fun setSelectedImage(bitmap: Bitmap) {
        selectedImageBitmap = bitmap
        mBinding.ivSelectedImage.setImageBitmap(selectedImageBitmap)
        mBinding.clSelectedImage.visibility = View.VISIBLE
        mBinding.ivCamera.visibility = View.GONE
        mBinding.clCamera.visibility = View.INVISIBLE
        mBinding.clCamera.isClickable = false
    }

    private fun clearSelectedImage() {
        selectedImageBitmap = null
        mBinding.ivSelectedImage.setImageResource(0)
        mBinding.clSelectedImage.visibility = View.GONE
        mBinding.ivCamera.visibility = View.VISIBLE
        mBinding.clCamera.visibility = View.VISIBLE
        mBinding.clCamera.isClickable = true
    }

    private fun setPic(uri: Uri, isEditMode: Boolean = false) {
        val inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream, null, Utility.getBitmapOptions())?.also { bitmap ->
            if (isEditMode) {
                editModeImageSelectionCallback?.invoke(bitmap)
            } else {
                setSelectedImage(bitmap)
            }
        }
    }

    private val getImageLauncherEditMode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            try {
                activityResult?.data?.data?.let { setPic(it, true) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            try {
                activityResult?.data?.data?.let { setPic(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun pickImageIntent(isEditMode: Boolean = false) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (isEditMode) {
            getImageLauncherEditMode.launch(Intent.createChooser(intent, "Select Picture"))
        } else {
            getImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun setPic(isEditMode: Boolean = false) {
        if (!::currentPhotoPath.isInitialized) return
        BitmapFactory.decodeFile(currentPhotoPath, Utility.getBitmapOptions())?.also { bitmap ->
            if (isEditMode) {
                editModeImageSelectionCallback?.invoke(bitmap)
            } else {
                setSelectedImage(bitmap)
            }
        }
    }

    private val getImageCameraLauncherEditMode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            try {
                if (!::currentPhotoPath.isInitialized) return@registerForActivityResult
                Utility.galleryAddPic(currentPhotoPath, this)
                setPic(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val getImageCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            try {
                if (!::currentPhotoPath.isInitialized) return@registerForActivityResult
                Utility.galleryAddPic(currentPhotoPath, this)
                setPic()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dispatchTakePictureIntent(isEditMode: Boolean) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.fststep.myshop.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    if (isEditMode) {
                        getImageCameraLauncherEditMode.launch(takePictureIntent)
                    } else {
                        getImageCameraLauncher.launch(takePictureIntent)
                    }
                }
            }
        }
    }

    private fun showImagePicker(isEditMode: Boolean, setSelectedImage: ((bitmap: Bitmap) -> Unit)?) {
        this.editModeImageSelectionCallback = setSelectedImage
        val dialog = ImagePickerDialog.newInstance(
            ::pickImageIntent,
            ::dispatchTakePictureIntent,
            isEditMode
        )
        dialog.show(supportFragmentManager, ImagePickerDialog.TAG)
    }

}