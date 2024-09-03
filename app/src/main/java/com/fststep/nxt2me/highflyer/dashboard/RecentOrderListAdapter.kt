/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.google.android.material.button.MaterialButton

/**
 * Created by Jay Kulshreshtha on 22/05/21.
 */
class RecentOrderListAdapter(var context: Context, private val dataSet: ArrayList<RecentOrderInfo>) :
    RecyclerView.Adapter<RecentOrderListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvOrderAddress: AppCompatTextView = view.findViewById(R.id.tvOrderAddress)
        var tvOrderType: AppCompatTextView = view.findViewById(R.id.tvOrderType)
        var tvOrderDate: AppCompatTextView = view.findViewById(R.id.tvOrderDate)
        var tvOrderTime: AppCompatTextView = view.findViewById(R.id.tvOrderTime)
        var tvOrderStatus: AppCompatTextView = view.findViewById(R.id.tvOrderStatus)
        var clOrder: ConstraintLayout = view.findViewById(R.id.clOrder)
        var clHiredDeliveryBoy: ConstraintLayout = view.findViewById(R.id.clHiredDeliveryBoy)
        var tvDeliveryBoyName: AppCompatTextView = view.findViewById(R.id.tvDeliveryBoyName)
        var buttonPayDeliveryBoy: MaterialButton = view.findViewById(R.id.buttonPayDeliveryBoy)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_recent_order_list, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvOrderAddress.text = dataSet[position].orderAddress
        viewHolder.tvOrderType.text = dataSet[position].orderType
        viewHolder.tvOrderDate.text = dataSet[position].orderDate
        viewHolder.tvOrderTime.text = dataSet[position].orderTime
        viewHolder.tvOrderStatus.text = dataSet[position].orderStatus
        if(dataSet[position].useDeliveryPerson) {
            viewHolder.clHiredDeliveryBoy.visibility = View.VISIBLE
            viewHolder.tvDeliveryBoyName.text = String.format(context.getString(R.string.lbl_hired_delivery_boy_name),dataSet[position].deliveryPerson?.name)
        }

        when {
            dataSet[position].orderStatus.contentEquals("Pending", true) -> {
                viewHolder.clHiredDeliveryBoy.visibility = View.GONE
                viewHolder.tvOrderStatus.setTextColor(Color.parseColor("#f05a28"))
            }
            dataSet[position].orderStatus.contentEquals("Accepted", true) -> {
                viewHolder.tvOrderStatus.setTextColor(Color.parseColor("#00adee"))
            }
            dataSet[position].orderStatus.contentEquals("Delivered", true) -> {
                viewHolder.tvOrderStatus.setTextColor(Color.parseColor("#00a550"))
            }
            dataSet[position].orderStatus.contentEquals("Rejected", true) -> {
                viewHolder.clHiredDeliveryBoy.visibility = View.GONE
                viewHolder.tvOrderStatus.setTextColor(Color.parseColor("#ec1d24"))
            }
        }

        // Define click listener for the ViewHolder's View.
//        viewHolder.clOrder.setOnClickListener {
//            val intent = Intent(context, OrderStatusActivity::class.java)
//            intent.putExtra("param_address", dataSet[position].orderAddress)
//            intent.putExtra("param_order_type", dataSet[position].orderType)
//            intent.putExtra("param_order_date", dataSet[position].orderDate)
//            intent.putExtra("param_order_time", dataSet[position].orderTime)
//            intent.putExtra("param_order_status", dataSet[position].orderStatus)
//            intent.putExtra("param_order_use_delivery_person", dataSet[position].useDeliveryPerson)
//            intent.putExtra("param_delivery_person_name", dataSet[position].deliveryPerson?.name)
//            context.startActivity(intent)
//        }

        viewHolder.buttonPayDeliveryBoy.setOnClickListener {
//            val intent = Intent(context, DeliveryStatusActivity::class.java)
//            context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
