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
import com.fststep.nxt2me.dashboard.model.BookingItemListInfo
import com.fststep.nxt2me.dashboard.view.adapter.BookingCartAdapter
import com.fststep.nxt2me.databinding.ActivityBookingCartBinding
import java.io.Serializable

class BookingCartActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityBookingCartBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bookingCartAdapter: BookingCartAdapter
    private lateinit var itemList: ArrayList<BookingItemListInfo>
    private var itemCount: Int = 0
    private var totalAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_booking_cart)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        itemList = ArrayList()
        itemList = intent.getSerializableExtra("itemlist") as ArrayList<BookingItemListInfo>

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvItemList.layoutManager = linearLayoutManager
        bookingCartAdapter = BookingCartAdapter(this,itemList)
        mBinding.rvItemList.adapter = bookingCartAdapter
        mBinding.rvItemList.addItemDecoration(SpaceItemDecoration(20))

        mBinding.btnBook.setOnClickListener {
            val intent = Intent(this,BookingSlotActivity::class.java).apply {
                putExtra("itemlist", itemList as Serializable)
            }
            startActivity(intent)
        }

        getItemCount()
        getTotalAmount()
    }

    private fun getItemCount() {
        itemCount += itemList.size
        mBinding.tvItemCount.text = itemCount.toString()
    }

    private fun getTotalAmount() {
        for (i in 0 until itemList.size) {
            totalAmount += itemList[i].merchantItemAmount
        }
        mBinding.tvTotalAmount.text = totalAmount.toString()
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