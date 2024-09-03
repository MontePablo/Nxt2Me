package com.fststep.nxt2me.dashboard.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.ActivityContactUsBinding

class ContactUsActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.etEmailAddress.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // code to execute when EditText loses focus
                if (!isValidEmail(mBinding.etEmailAddress.text.toString())) {
                    mBinding.etEmailAddress.error = "Invalid email"
                }
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}
