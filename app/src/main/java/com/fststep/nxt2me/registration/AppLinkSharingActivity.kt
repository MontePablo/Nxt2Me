package com.fststep.nxt2me.registration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.ActivityAppLinkSharingBinding
import com.fststep.nxt2me.login.LoginActivity
import java.net.URLEncoder

/**
 * Created by Jay Kulshreshtha on 26/06/21.
 */
class AppLinkSharingActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityAppLinkSharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_app_link_sharing)

        mBinding.btnShare.setOnClickListener {
            shareLinkWithSpecificContact("Referral Code")
        }
        mBinding.btnSkip.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }

    private fun shareLinkWithSpecificContact(referralCode:String) {
        val query: String = URLEncoder.encode(referralCode, "utf-8")
        val uri = Uri.parse("https://wa.me/?text=$query")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}