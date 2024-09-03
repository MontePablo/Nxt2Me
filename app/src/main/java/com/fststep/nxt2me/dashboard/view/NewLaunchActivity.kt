package com.fststep.nxt2me.dashboard.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.ActivityNewLaunchBinding

class NewLaunchActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityNewLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_launch)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

}