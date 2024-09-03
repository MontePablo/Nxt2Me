package com.fststep.nxt2me.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.login.LoginActivity
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.ActivityWelcomeBinding
import com.fststep.nxt2me.registration.NewRegistrationActivity

/**
 * Created by Shubham Singh on 26/06/21.
 */
class WelcomeActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        mBinding.buttonRegistration.setOnClickListener {
            startActivity(Intent(this, NewRegistrationActivity::class.java))
        }

        mBinding.buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}