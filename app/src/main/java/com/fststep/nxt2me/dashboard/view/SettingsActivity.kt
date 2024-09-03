package com.fststep.nxt2me.dashboard.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.ChangePasswordRequest
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.view.CommonDialogs
import com.fststep.nxt2me.databinding.ActivitySettingsBinding
import com.fststep.nxt2me.databinding.DialogChangePasswordBinding
import com.fststep.nxt2me.login.UserViewModel
import com.fststep.nxt2me.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SettingsActivity: AppCompatActivity() {
    lateinit var dialog: AlertDialog
    private lateinit var mBinding: ActivitySettingsBinding
    
    val commonDialogs= CommonDialogs()
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        userViewModel.apply {
            observe(changePasswordResponse, ::onChangePassword)
        }

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.ivShopStatusActive.setOnClickListener {
            mBinding.ivShopStatusActive.visibility = View.GONE
            mBinding.ivShopStatusDeactive.visibility = View.VISIBLE
        }

        mBinding.ivShopStatusDeactive.setOnClickListener {
            mBinding.ivShopStatusActive.visibility = View.VISIBLE
            mBinding.ivShopStatusDeactive.visibility = View.GONE
        }

        mBinding.tvResetPassword.setOnClickListener {
            val binding= DialogChangePasswordBinding.inflate(layoutInflater)
            dialog=commonDialogs.dialogBuild(this,binding)
            binding.apply {
                cancelbtn.setOnClickListener { dialog.dismiss() }
                savebtn.setOnClickListener {  userViewModel.changePassword(ChangePasswordRequest(Preferences.fetchUser()?.id,oldPassword.text.toString(),newPassword.text.toString())) }
            }
        }
        mBinding.apply {
            tvLogout.setOnClickListener(View.OnClickListener {
                Preferences.clearAllAccountData()
                startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                finish()
            })
        }

        mBinding.tvTnC.setOnClickListener {

        }

        mBinding.tvFAQ.setOnClickListener {

        }

        mBinding.tvKnowApplication.setOnClickListener {

        }
    }

    private fun onChangePassword(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                Log.d(this.localClassName,"term update success")
                if(dialog!=null)
                    Toast.makeText(this,"password changed!",Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
            }
            else -> {}
        }
    }

}