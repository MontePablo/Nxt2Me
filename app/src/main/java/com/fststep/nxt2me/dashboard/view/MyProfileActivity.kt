package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.databinding.ActivityMyProfileBinding
import com.fststep.nxt2me.login.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileActivity: AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var mBinding: ActivityMyProfileBinding

    private var editMode=false
    private var isSharingDone: Boolean = false

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        userViewModel.apply {
            observe(updateUserResponse,::onUpdateUser)
        }
        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }
        mBinding.ivShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Dear Customer,We are happy to announce that " + Preferences.fetchHighFlyerAccount()?.businessName.toString() + " is now available Online! Download the below app and use my referral code - " + mBinding.tvReferralCodeValue.text.toString() + "for registration." + "# My Shop google link#")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share your shop")
            startActivity(shareIntent)

            isSharingDone = true
        }

        val user=Preferences.fetchUser()

        mBinding.apply {
            user?.apply {
                tvNameValue.setText(customerName)
                tvMobileNumberValue.setText(mobileNo)
                tvFlatNumberValue.setText(wingFlatNo)
                tvSocietyNameValue.setText(societyName)
            }

            buttonEdit.setOnClickListener {
                if(editMode){
                    editMode=false
                    buttonEdit.text= getString(R.string.lbl_edit)
                    user?.apply {
                        customerName=tvNameValue.text.toString()
                        mobileNo=tvMobileNumberValue.text.toString()
                        wingFlatNo=tvFlatNumberValue.text.toString()
                        societyName=tvSocietyNameValue.text.toString()
                    }
                    userViewModel.updateUser(user!!)
                    Utility.disableFocus(arrayListOf(tvNameValue,tvSocietyNameValue,tvFlatNumberValue,tvMobileNumberValue))
                }else{
                    editMode=true
                    buttonEdit.text= getString(R.string.save)
                    Utility.enableFocus(arrayListOf(tvNameValue,tvSocietyNameValue,tvFlatNumberValue,tvMobileNumberValue))
                }
            }
        }
    }

    private fun onUpdateUser(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                Toast.makeText(applicationContext,getString(R.string.success),Toast.LENGTH_SHORT).show()
                Log.d(this.localClassName,"user update success")
            }
            else -> {}
        }
    }

    override fun onResume() {
        super.onResume()

        if (isSharingDone) {
            val serviceType = Preferences.fetchHighFlyerAccount()?.serviceType
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.KEY_SERVICE_TYPE, serviceType)
            startActivity(intent)
            finishAffinity()
        }
    }

}

