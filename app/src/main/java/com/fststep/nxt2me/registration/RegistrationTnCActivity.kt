package com.fststep.nxt2me.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.fststep.nxt2me.databinding.ActivityRegistrationTncBinding
import com.fststep.nxt2me.splash.TermsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Jay Kulshreshtha on 26/06/21.
 */
@AndroidEntryPoint
class RegistrationTnCActivity: AppCompatActivity() {
    private val termsViewModel: TermsViewModel by viewModels()
    
    private lateinit var mBinding: ActivityRegistrationTncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration_tnc)
        termsViewModel.apply {
            observe(acceptTermsResponse, ::onAcceptTermsPerform)
        }
        mBinding.btnIAgree.setOnClickListener {
            termsViewModel.acceptTerms(Constants.CUSTOMER,Preferences.fetchUser()!!.id!!)
        }
        mBinding.TnC.text=(Preferences.fetchTerms()?.data!!.filter { term -> term.userType==Constants.CUSTOMER })[0].termsCondition
    }
    fun onAcceptTermsPerform(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                startActivity(Intent(this,AppLinkSharingActivity::class.java))
                Log.d(this.localClassName,"term update success")
                finish()

            }
            else -> {}
        }
    }
}


