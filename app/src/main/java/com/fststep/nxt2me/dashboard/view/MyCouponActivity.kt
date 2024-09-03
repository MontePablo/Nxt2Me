package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MyCouponItemInfo
import com.fststep.nxt2me.dashboard.view.adapter.MyCouponItemListAdapter
import com.fststep.nxt2me.databinding.ActivityMyCouponBinding

class MyCouponActivity: AppCompatActivity() {

    private lateinit var mBinding:ActivityMyCouponBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var myCouponItemListAdapter: MyCouponItemListAdapter
    private lateinit var couponList: MutableList<MyCouponItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_coupon)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.btnGenerateCoupon.setOnClickListener {
            startActivity(Intent(this,GenerateCouponActivity::class.java))
        }

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvCoupon.layoutManager = linearLayoutManager
        couponList = ArrayList()
        dummyData()
        myCouponItemListAdapter = MyCouponItemListAdapter(this,
            couponList as ArrayList<MyCouponItemInfo>
        )
        mBinding.rvCoupon.adapter = myCouponItemListAdapter
        mBinding.rvCoupon.addItemDecoration(SpaceItemDecoration(20))
    }

    private fun dummyData() {
        couponList.add(MyCouponItemInfo(R.drawable.ic_groceries,"Cold Stone","5","Min order Rs 300","3rd Apr"))
        couponList.add(MyCouponItemInfo(R.drawable.ic_groceries,"Cold Stone","5","Min order Rs 300","3rd Apr"))
        couponList.add(MyCouponItemInfo(R.drawable.ic_groceries,"Cold Stone","5","Min order Rs 300","3rd Apr"))
        couponList.add(MyCouponItemInfo(R.drawable.ic_groceries,"Cold Stone","5","Min order Rs 300","3rd Apr"))
        couponList.add(MyCouponItemInfo(R.drawable.ic_groceries,"Cold Stone","5","Min order Rs 300","3rd Apr"))
    }

    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

}