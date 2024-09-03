/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.fststep.nxt2me.databinding.FragmentRecentOrderBinding
import com.fststep.nxt2me.highflyer.registration.ServiceType

/**
 * Created by Jay Kulshreshtha on 22/05/21.
 */
class RecentOrderFragment : Fragment() {

    private lateinit var mBinding: FragmentRecentOrderBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recentOrderListAdapter: RecentOrderListAdapter
    private lateinit var recentOrderList: MutableList<RecentOrderInfo>

    private var serviceType: ServiceType? = null

    companion object {
        fun newInstance(serviceType: ServiceType): RecentOrderFragment {
            val frag = RecentOrderFragment()
            frag.serviceType = serviceType
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mBinding = FragmentRecentOrderBinding.inflate(layoutInflater)
        val recentOrder: View = mBinding.root

        linearLayoutManager = LinearLayoutManager(activity)
        mBinding.rvRecentOrderList.layoutManager = linearLayoutManager
        recentOrderList = ArrayList()
        dummyData()
        recentOrderListAdapter = RecentOrderListAdapter(requireContext(), recentOrderList as ArrayList<RecentOrderInfo>)
        mBinding.rvRecentOrderList.adapter = recentOrderListAdapter
        mBinding.rvRecentOrderList.addItemDecoration(SpacesItemDecoration(15))

        mBinding.buttonHireDeliveryBoy.setOnClickListener {
//            val intent = Intent(requireContext(), HireDeliveryBoyActivity::class.java)
//            requireActivity().startActivity(intent)
        }

        return recentOrder
    }

    class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.bottom = space
        }
    }

    private fun dummyData() {
        recentOrderList.add(
            RecentOrderInfo(
                "Raj Desai -  Pushpa Vatika B101", "Home Delivery",
                "3/3/21", "06:34 pm", "Pending",
                useDeliveryPerson = true,
                deliveryPerson = DeliveryPerson(0,"Ravi Shankar","1234567890", "150")
            )
        )
        recentOrderList.add(
            RecentOrderInfo(
                "Raj Desai -  Pushpa Vatika B101", "Home Delivery",
                "3/3/21", "06:34 pm", "Accepted"
            )
        )
        recentOrderList.add(
            RecentOrderInfo(
                "Raj Desai -  Pushpa Vatika B101", "Home Delivery",
                "3/3/21", "06:34 pm", "Delivered"
            )
        )
        recentOrderList.add(
            RecentOrderInfo(
                "Raj Desai -  Pushpa Vatika B101", "Home Delivery",
                "3/3/21", "06:34 pm", "Delivered"
            )
        )
        recentOrderList.add(
            RecentOrderInfo(
                "Raj Desai -  Pushpa Vatika B101", "Home Delivery",
                "3/3/21", "06:34 pm", "Rejected"
            )
        )
    }
}
