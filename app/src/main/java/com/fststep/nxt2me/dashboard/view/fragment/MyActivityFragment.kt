package com.fststep.nxt2me.dashboard.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.dashboard.model.MyActivityItemInfo
import com.fststep.nxt2me.dashboard.view.adapter.MyActivityItemListAdapter
import com.fststep.nxt2me.databinding.FragmentMyActivityBinding

class MyActivityFragment:Fragment() {

    private lateinit var mBinding: FragmentMyActivityBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var orderItemList: MutableList<MyActivityItemInfo>
    private lateinit var myActivityItemListAdapter: MyActivityItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mBinding = FragmentMyActivityBinding.inflate(layoutInflater)
        val myactivity: View = mBinding.root

        linearLayoutManager = LinearLayoutManager(requireContext())
        mBinding.rvOrder.layoutManager = linearLayoutManager
        orderItemList = ArrayList()
        dummyData()
        myActivityItemListAdapter = MyActivityItemListAdapter(requireContext(),
            orderItemList as ArrayList<MyActivityItemInfo>)
        mBinding.rvOrder.adapter = myActivityItemListAdapter
        mBinding.rvOrder.addItemDecoration(SpaceItemDecoration(20))

        return myactivity
    }

    private fun dummyData() {
        orderItemList.add(MyActivityItemInfo("3/3/21","Order Grocery from Shiv Raj Mohan store","397","pending"))
        orderItemList.add(MyActivityItemInfo("3/3/21","Booked Beauty parlour Fair and Glow","397","pending"))
        orderItemList.add(MyActivityItemInfo("3/3/21","Connected Tution teacher Miss Shradha Kumari","","booked"))
        orderItemList.add(MyActivityItemInfo("3/3/21","Food order from Shiva Tiffin service","397","booked"))
        orderItemList.add(MyActivityItemInfo("3/3/21","Order Grocery from Shiv Raj Mohan store","397","completed"))
        orderItemList.add(MyActivityItemInfo("3/3/21","Order Grocery from Shiv Raj Mohan store","397","completed"))
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