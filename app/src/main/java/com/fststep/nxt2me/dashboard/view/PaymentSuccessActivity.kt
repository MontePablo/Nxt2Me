package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fststep.nxt2me.databinding.ActivityPaymentSucessBinding

class PaymentSuccessActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityPaymentSucessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityPaymentSucessBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.ivBackArrow.setOnClickListener {
            startActivity(Intent(this@PaymentSuccessActivity,DashboardActivity::class.java))
            finishAffinity()
        }
        mBinding.btnDone.setOnClickListener {
            startActivity(Intent(this@PaymentSuccessActivity,DashboardActivity::class.java))
            finishAffinity()
        }

    }
}