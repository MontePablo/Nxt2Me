package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.ActivityExtrasBinding
import java.net.URLEncoder

class ExtrasActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityExtrasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_extras)

        mBinding.llMyRewards.setOnClickListener {
            startActivity(Intent(this,MyRewardsActivity::class.java))
        }

        mBinding.llNewLaunch.setOnClickListener {
            startActivity(Intent(this,NewLaunchActivity::class.java))
        }

        mBinding.llMyCoupon.setOnClickListener {
            startActivity(Intent(this,MyCouponActivity::class.java))
        }

        mBinding.llPromotionOffer.setOnClickListener {
            startActivity(Intent(this,PromotionOffersActivity::class.java))
        }

        mBinding.llMeetPeople.setOnClickListener {
            startActivity(Intent(this,MeetPeopleActivity::class.java))
        }

        mBinding.llEventArea.setOnClickListener {
            startActivity(Intent(this,EventsInYourAreaActivity::class.java))
        }

        mBinding.llViewCategories.setOnClickListener {
            Toast.makeText(this,"Coming soon!",Toast.LENGTH_SHORT).show()
        }

        mBinding.llBuyAgain.setOnClickListener {
            Toast.makeText(this,"Coming soon!",Toast.LENGTH_SHORT).show()
        }

        mBinding.llYourOrder.setOnClickListener {
            Toast.makeText(this,"Coming soon!",Toast.LENGTH_SHORT).show()
        }

        mBinding.llMyPromotion.setOnClickListener {
            Toast.makeText(this,"Coming soon!",Toast.LENGTH_SHORT).show()
        }

        mBinding.llSetLanguage.setOnClickListener {
            Toast.makeText(this,"Coming soon!",Toast.LENGTH_SHORT).show()
        }

        mBinding.llShareFriends.setOnClickListener {
            shareLinkWithSpecificContact("Referral Code")
        }

        mBinding.llSettings.setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }

        mBinding.llMyProfile.setOnClickListener {
            startActivity(Intent(this,MyProfileActivity::class.java))
        }

        mBinding.llContactUs.setOnClickListener {
            startActivity(Intent(this,ContactUsActivity::class.java))
        }
    }

    private fun shareLinkWithSpecificContact(referralCode:String) {
        val query: String = URLEncoder.encode(referralCode, "utf-8")
        val uri = Uri.parse("https://wa.me/?text=$query")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}