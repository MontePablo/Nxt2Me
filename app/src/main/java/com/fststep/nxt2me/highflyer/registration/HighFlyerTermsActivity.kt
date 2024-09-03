package com.fststep.nxt2me.highflyer.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.dashboard.view.DashboardActivity
import com.fststep.nxt2me.databinding.ActivityHighFlyerTermsBinding

import com.fststep.nxt2me.splash.TermsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Shubham Singh on 01/08/21.
 */
@AndroidEntryPoint
class HighFlyerTermsActivity: AppCompatActivity() {
    private val termsViewModel: TermsViewModel by viewModels()
    private lateinit var mBinding: ActivityHighFlyerTermsBinding

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_high_flyer_terms)

        mBinding.toolbar.btBack.setOnClickListener {
            onBackPressed()
        }
        termsViewModel.apply {
            observe(acceptTermsResponse, ::onAcceptTermsPerform)
        }
        mBinding.TnC.text=(Preferences.fetchTerms()?.data!!.filter { term -> term.userType==Constants.HIGHFLYER })[0].termsCondition

        mBinding.buttonNext.setOnClickListener {
            termsViewModel.acceptTerms(Constants.HIGHFLYER,Preferences.fetchUser()!!.id!!)
        }
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
                startActivity(Intent(this@HighFlyerTermsActivity,DashboardActivity::class.java).putExtra(Constants.KEY_DASHBOARD_JUMP,2))
                Log.d(this.localClassName,"term update success")
                finishAffinity()

            }
            else -> {}
        }
    }
}