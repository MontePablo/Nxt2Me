package com.fststep.nxt2me.highflyer.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.databinding.ActivityHighFlyerSubscriptionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Shubham Singh on 01/08/21.
 */
@AndroidEntryPoint
class HighFlyerSubscription: AppCompatActivity() {

    private lateinit var mBinding: ActivityHighFlyerSubscriptionBinding
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_high_flyer_subscription)

        mBinding.apply {
            buttonMonthly.setOnClickListener { nextPage() }
            buttonYearly.setOnClickListener { nextPage() }
        }
    }


    fun nextPage(){

    }
}