package com.fststep.nxt2me.highflyer.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.view.DeactivateDialog
import com.fststep.nxt2me.databinding.FragmentDashboardBinding
import com.fststep.nxt2me.highflyer.registration.ServiceType
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Shubham Singh on 10/10/21.
 */
@AndroidEntryPoint
class HfDashboardFragment: Fragment() {

    private lateinit var mBinding: FragmentDashboardBinding
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private lateinit var hfDashboardViewPagerAdapter: HfDashboardViewPagerAdapter
    private lateinit var serviceType: CategoryTypeEnum
    private var advertismentSubscribed: Boolean = false

    


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        serviceType = Preferences.fetchMySubCat()?.categoryType?.getCategoryTypeEnum()!!
        tabLayout = mBinding.tlDashboard
        viewPager = mBinding.vpDashboard
        tabLayout?.setupWithViewPager(viewPager)

        mBinding.ivShopStatusActive.setOnClickListener {
            mBinding.ivShopStatusActive.visibility = View.GONE
            mBinding.ivShopStatusDeactive.visibility = View.VISIBLE
            DeactivateDialog.newInstance().show(childFragmentManager, DeactivateDialog.TAG)
            mBinding.tvShopStatus.text = getString(R.string.lbl_deactive)
        }

        mBinding.ivShopStatusDeactive.setOnClickListener {
            mBinding.ivShopStatusActive.visibility = View.VISIBLE
            mBinding.ivShopStatusDeactive.visibility = View.GONE
            mBinding.tvShopStatus.text = getString(R.string.lbl_active)
        }

        viewPager?.let { setupViewPager(it) }
        setupTabIcons()
    }

    private fun setupViewPager(viewpager: ViewPager) {
        if (!::serviceType.isInitialized) return
        hfDashboardViewPagerAdapter = HfDashboardViewPagerAdapter(childFragmentManager)

        when (serviceType) {
            CategoryTypeEnum.Good -> initGoodsDeliveryFrags()
            CategoryTypeEnum.Booking -> initBookingServiceFrags()
            CategoryTypeEnum.PhoneNumber -> initPhoneServiceFrags()
            CategoryTypeEnum.Delivery -> initDeliveryAgentFrags()

            else -> {}
        }

        // setting adapter to view pager.
        viewpager.adapter = hfDashboardViewPagerAdapter
    }

    private fun initGoodsDeliveryFrags() {
        hfDashboardViewPagerAdapter.addFragment(MenuFragment(), "Menu")
        hfDashboardViewPagerAdapter.addFragment(RecentOrderFragment(), "Recent Order")
        hfDashboardViewPagerAdapter.addFragment(EarningsFragment(), "Earnings")
        hfDashboardViewPagerAdapter.addFragment(PromotionFragment(), "Promotion")
    }
    private fun initDeliveryAgentFrags() {
        hfDashboardViewPagerAdapter.addFragment(MenuFragment(), "New Order")
        hfDashboardViewPagerAdapter.addFragment(RecentOrderFragment(), "History")
        hfDashboardViewPagerAdapter.addFragment(EarningsFragment(), "Earnings")
    }

    private fun initBookingServiceFrags() {
        hfDashboardViewPagerAdapter.addFragment(MenuFragment(), "Services")
        hfDashboardViewPagerAdapter.addFragment(RecentOrderFragment(), "Appointments")
        hfDashboardViewPagerAdapter.addFragment(EarningsFragment(), "Earnings")
        hfDashboardViewPagerAdapter.addFragment(PromotionFragment(), "Promotion")
    }

    private fun initPhoneServiceFrags() {
        hfDashboardViewPagerAdapter.addFragment(MenuFragment(), "Services")
        hfDashboardViewPagerAdapter.addFragment(RecentOrderFragment(), "People may have reached you")
    }

    private fun setupTabIcons() {
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}