package com.fststep.nxt2me.highflyer.dashboard

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
import com.fststep.nxt2me.databinding.LayoutMenuDeleteDialogBinding

/**
 * Created by Shubham Singh on 14/08/21.
 */
class DeleteConfirmationDialog: DialogFragment() {

    private lateinit var mBinding: LayoutMenuDeleteDialogBinding
    private lateinit var positiveListener: View.OnClickListener

    companion object {
        const val TAG = "MenuDeleteDialog"

        fun newInstance(): DeleteConfirmationDialog {
            return DeleteConfirmationDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_menu_delete_dialog, container, false)
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        mBinding.buttonYes.setOnClickListener(positiveListener)
        mBinding.buttonNo.setOnClickListener {
            dismiss()
        }
        return mBinding.root
    }

    fun setPositiveListener(positiveListener: View.OnClickListener) {
        this.positiveListener = positiveListener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.CENTER)
    }
}