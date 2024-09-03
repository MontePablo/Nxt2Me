package com.fststep.nxt2me.dashboard.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MyActivityItemDetailInfo
import com.fststep.nxt2me.dashboard.view.adapter.MyActivityDetailListAdapter
import com.fststep.nxt2me.databinding.ActivityMyActivityItemDetailsBinding

class MyActivityItemDetailsActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityMyActivityItemDetailsBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemList: MutableList<MyActivityItemDetailInfo>
    private lateinit var itemListAdapter: MyActivityDetailListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_activity_item_details)

        val status = intent.getStringExtra("status")
        if (status == "pending") {
            mBinding.clStatusPending.visibility = View.VISIBLE
        } else if (status == "booked") {
            mBinding.clStatusBooked.visibility = View.VISIBLE
        } else if (status == "completed") {
            mBinding.btnReOrder.visibility = View.VISIBLE
        }

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvItemList.layoutManager = linearLayoutManager
        itemList = ArrayList()
        dummyData()
        itemListAdapter = MyActivityDetailListAdapter(this, itemList as ArrayList<MyActivityItemDetailInfo>)
        mBinding.rvItemList.adapter = itemListAdapter
        mBinding.rvItemList.addItemDecoration(SpaceItemDecoration(10))
    }

    private fun dummyData() {
        itemList.add(MyActivityItemDetailInfo("Sugar","1","50"))
        itemList.add(MyActivityItemDetailInfo("Wheat flour","6","240"))
        itemList.add(MyActivityItemDetailInfo("Rice","2.5","100"))
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