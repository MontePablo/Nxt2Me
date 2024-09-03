package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MyActivityItemDetailInfo

class MyActivityDetailListAdapter(var context: Context, private val dataSet: ArrayList<MyActivityItemDetailInfo>) :
    RecyclerView.Adapter<MyActivityDetailListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvSerialNumber: AppCompatTextView = view.findViewById(R.id.tvSerialNumber)
        var tvItemName: AppCompatTextView = view.findViewById(R.id.tvItemName)
        var tvItemWeight: AppCompatTextView = view.findViewById(R.id.tvItemWeight)
        var tvItemAmount: AppCompatTextView = view.findViewById(R.id.tvItemAmount)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_my_activity_detail_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvSerialNumber.text = position.toString() + "."
        viewHolder.tvItemName.text = dataSet[position].itemName
        viewHolder.tvItemWeight.text = dataSet[position].itemWeight + "kg"
        viewHolder.tvItemAmount.text = "₹ " + dataSet[position].itemAmount + "/-"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}