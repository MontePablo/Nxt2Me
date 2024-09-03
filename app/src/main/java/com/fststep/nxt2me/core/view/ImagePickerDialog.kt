/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.LayoutImagePickerBinding

/**
 * Created by Shubham Singh on 28/05/21.
 */
class ImagePickerDialog(private val galleryHandler: (isEditMode: Boolean) -> Unit, private val cameraHandler: (isEditMode: Boolean) -> Unit, private val isEditMode: Boolean) : DialogFragment(), View.OnClickListener {

    private lateinit var mBinding: LayoutImagePickerBinding

    companion object {

        const val TAG = "ImagePickerDialog"

        fun newInstance(galleryHandler: (isEditMode: Boolean) -> Unit, cameraHandler: (isEditMode: Boolean) -> Unit, isEditMode: Boolean = false): ImagePickerDialog {
            return ImagePickerDialog(galleryHandler, cameraHandler, isEditMode)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_image_picker, container, false)
        mBinding.handler = this
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onClick(v: View?) {
        dismiss()
        when (v?.id) {
            R.id.layoutGalleryPicker -> galleryHandler.invoke(isEditMode)
            R.id.layoutCamera -> cameraHandler.invoke(isEditMode)
        }
    }
}
