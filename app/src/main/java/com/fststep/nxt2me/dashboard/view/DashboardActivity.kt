package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.fststep.myshop.core.view.adapter.DashboardViewPagerAdapter
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.dashboard.view.fragment.ChatFragment
import com.fststep.nxt2me.dashboard.view.fragment.HighFlyerFaqFragment
import com.fststep.nxt2me.dashboard.view.fragment.HomeFragment
import com.fststep.nxt2me.dashboard.view.fragment.MyActivityFragment
import com.fststep.nxt2me.databinding.ActivityDashboardBinding
import com.fststep.nxt2me.highflyer.registration.HighFlyerRegistration
import com.fststep.nxt2me.highflyer.HighFlyerFragment
import com.fststep.nxt2me.registration.RegistrationTnCActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityDashboardBinding
    private var viewPager: ViewPager? = null
    private lateinit var dashboardViewPagerAdapter: DashboardViewPagerAdapter
    var dashboardJumpTo:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        dashboardJumpTo=intent.getIntExtra(Constants.KEY_DASHBOARD_JUMP,-1)
        viewPager = mBinding.vpDashboard
        viewPager?.let { setupViewPager(it) }

        mBinding.clHome.setOnClickListener {
            mBinding.vHome.visibility = View.VISIBLE
            mBinding.vMyActivity.visibility = View.GONE
            mBinding.vHighFlyer.visibility = View.GONE
            mBinding.vChat.visibility = View.GONE
            viewPager!!.setCurrentItem(0,true)
        }

        mBinding.clMyActivity.setOnClickListener {
            mBinding.vHome.visibility = View.GONE
            mBinding.vMyActivity.visibility = View.VISIBLE
            mBinding.vHighFlyer.visibility = View.GONE
            mBinding.vChat.visibility = View.GONE
            viewPager!!.setCurrentItem(1,true)
        }

        mBinding.clHighFlyer.setOnClickListener {
            mBinding.vHome.visibility = View.GONE
            mBinding.vMyActivity.visibility = View.GONE
            mBinding.vHighFlyer.visibility = View.VISIBLE
            mBinding.vChat.visibility = View.GONE
            viewPager!!.setCurrentItem(2,true)
        }

        mBinding.clChat.setOnClickListener {
            mBinding.vHome.visibility = View.GONE
            mBinding.vMyActivity.visibility = View.GONE
            mBinding.vHighFlyer.visibility = View.GONE
            mBinding.vChat.visibility = View.VISIBLE
            viewPager!!.setCurrentItem(3,true)
        }

        mBinding.clPlus.setOnClickListener {
            startActivity(Intent(this,ExtrasActivity::class.java))
        }
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.
                if(position == 0) {
                    mBinding.vHome.visibility = View.VISIBLE
                    mBinding.vMyActivity.visibility = View.GONE
                    mBinding.vHighFlyer.visibility = View.GONE
                    mBinding.vChat.visibility = View.GONE
                } else if(position == 1) {
                    mBinding.vHome.visibility = View.GONE
                    mBinding.vMyActivity.visibility = View.VISIBLE
                    mBinding.vHighFlyer.visibility = View.GONE
                    mBinding.vChat.visibility = View.GONE
                } else if(position == 2) {
                    mBinding.vHome.visibility = View.GONE
                    mBinding.vMyActivity.visibility = View.GONE
                    mBinding.vHighFlyer.visibility = View.VISIBLE
                    mBinding.vChat.visibility = View.GONE
                } else if(position == 3) {
                    mBinding.vHome.visibility = View.GONE
                    mBinding.vMyActivity.visibility = View.GONE
                    mBinding.vHighFlyer.visibility = View.GONE
                    mBinding.vChat.visibility = View.VISIBLE
                }
            }
        })
        if(dashboardJumpTo!=-1)
            viewPager!!.currentItem=dashboardJumpTo
    }

    private fun setupViewPager(viewpager: ViewPager) {
        dashboardViewPagerAdapter = DashboardViewPagerAdapter(supportFragmentManager)

        dashboardViewPagerAdapter.addFragment(HomeFragment())
        dashboardViewPagerAdapter.addFragment(MyActivityFragment())
        dashboardViewPagerAdapter.addFragment(HighFlyerFragment())
        dashboardViewPagerAdapter.addFragment(ChatFragment())

        // setting adapter to view pager.
        viewpager.adapter = dashboardViewPagerAdapter
    }

}