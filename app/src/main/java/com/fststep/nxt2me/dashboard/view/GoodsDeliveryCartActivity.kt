package com.fststep.nxt2me.dashboard.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.GoodsDeliveryCartItemListInfo
import com.fststep.nxt2me.dashboard.view.adapter.GoodsDeliveryCartAdapter
import com.fststep.nxt2me.databinding.ActivityGoodsDeliveryCartBinding

class GoodsDeliveryCartActivity: AppCompatActivity(), GoodsDeliveryCartAdapter.onButtonClickListener {

    private lateinit var mBinding: ActivityGoodsDeliveryCartBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var goodsDeliveryCartAdapter: GoodsDeliveryCartAdapter
    private lateinit var itemList: ArrayList<GoodsDeliveryCartItemListInfo>
    private var itemCount: Int = 0
    private var totalAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_delivery_cart)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        itemList = ArrayList()
        itemList = intent.getSerializableExtra("itemlist") as ArrayList<GoodsDeliveryCartItemListInfo>

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvItemList.layoutManager = linearLayoutManager
        goodsDeliveryCartAdapter = GoodsDeliveryCartAdapter(this,itemList,this)
        mBinding.rvItemList.adapter = goodsDeliveryCartAdapter
        mBinding.rvItemList.addItemDecoration(SpaceItemDecoration(20))

        getItemCount()
        getTotalAmount()
    }

    private fun getItemCount() {
        itemCount += itemList.size
        mBinding.tvItemCount.text = itemCount.toString()
        itemCount = 0
    }

    private fun getTotalAmount() {
        for (i in 0 until itemList.size) {
            totalAmount += (itemList[i].itemAmount * itemList[i].itemCount.toInt())
        }
        mBinding.tvTotalAmount.text = totalAmount.toString()
        totalAmount = 0
    }

    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

    override fun addItem() {
        getItemCount()
        getTotalAmount()
    }

    override fun removeItem() {
        getItemCount()
        getTotalAmount()
    }

    override fun deleteItem() {
        getItemCount()
        getTotalAmount()
    }

}