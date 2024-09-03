package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MyCouponItemInfo

class MyCouponItemListAdapter(var context: Context, private val dataSet: ArrayList<MyCouponItemInfo>) :
    RecyclerView.Adapter<MyCouponItemListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: AppCompatImageView = view.findViewById(R.id.ivImage)
        var tvName: AppCompatTextView = view.findViewById(R.id.tvName)
        var tvCondition: AppCompatTextView = view.findViewById(R.id.tvCondition)
        var tvDiscount: AppCompatTextView = view.findViewById(R.id.tvDiscount)
        var tvDate: AppCompatTextView = view.findViewById(R.id.tvDate)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_my_coupon_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ivImage.setImageDrawable(ContextCompat.getDrawable(context,dataSet[position].image))
        viewHolder.tvName.text = dataSet[position].shopName
        viewHolder.tvCondition.text = dataSet[position].condition
        viewHolder.tvDiscount.text = dataSet[position].discount + context.getString(R.string.lbl_percent_discount)
        viewHolder.tvDate.text = context.getString(R.string.lbl_valid_till) + dataSet[position].date
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}