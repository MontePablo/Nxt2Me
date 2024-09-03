package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.BookingItemListInfo

class BookingCartAdapter(var context: Context, private val dataSet: ArrayList<BookingItemListInfo>) :
    RecyclerView.Adapter<BookingCartAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivMerchantItemImage: AppCompatImageView = view.findViewById(R.id.ivMerchantItemImage)
        var tvMerchantItemName: AppCompatTextView = view.findViewById(R.id.tvMerchantItemName)
        var tvMerchantItemAmount: AppCompatTextView = view.findViewById(R.id.tvMerchantItemAmount)
        var cbMerchantItemCheckBox: AppCompatCheckBox = view.findViewById(R.id.cbMerchantItemCheckBox)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.customview_booking_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ivMerchantItemImage.setImageDrawable(ContextCompat.getDrawable(context,dataSet[position].merchantItemImage))
        viewHolder.tvMerchantItemName.text = dataSet[position].merchantItemName
        viewHolder.tvMerchantItemAmount.text = "₹ " + dataSet[position].merchantItemAmount
        viewHolder.cbMerchantItemCheckBox.visibility = View.GONE
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}