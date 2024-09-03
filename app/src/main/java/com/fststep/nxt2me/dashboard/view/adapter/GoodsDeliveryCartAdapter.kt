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
import com.fststep.nxt2me.dashboard.model.GoodsDeliveryCartItemListInfo

class GoodsDeliveryCartAdapter(var context: Context, private val dataSet: ArrayList<GoodsDeliveryCartItemListInfo>, var onButtonClick: onButtonClickListener) :
    RecyclerView.Adapter<GoodsDeliveryCartAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivMerchantItemImage: AppCompatImageView = view.findViewById(R.id.ivMerchantItemImage)
        var tvMerchantItemName: AppCompatTextView = view.findViewById(R.id.tvMerchantItemName)
        var tvMerchantItemAmount: AppCompatTextView = view.findViewById(R.id.tvMerchantItemAmount)
        var ivDeleteItem: AppCompatImageView = view.findViewById(R.id.ivDeleteItem)
        var tvMinus: AppCompatTextView = view.findViewById(R.id.tvMinus)
        var tvItemCount: AppCompatTextView = view.findViewById(R.id.tvItemCount)
        var tvAdd: AppCompatTextView = view.findViewById(R.id.tvAdd)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.customview_goods_card_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ivMerchantItemImage.setImageDrawable(ContextCompat.getDrawable(context,dataSet[position].itemImage))
        viewHolder.tvMerchantItemName.text = dataSet[position].itemName
        viewHolder.tvMerchantItemAmount.text = "₹ " + dataSet[position].itemAmount
        viewHolder.tvItemCount.text = dataSet[position].itemCount

        viewHolder.tvAdd.setOnClickListener {
            dataSet[position].itemCount = (dataSet[position].itemCount.toInt() + 1).toString()
            notifyDataSetChanged()
            onButtonClick.addItem()
        }

        viewHolder.tvMinus.setOnClickListener {
            dataSet[position].itemCount = (dataSet[position].itemCount.toInt() - 1).toString()
            notifyDataSetChanged()
            if (viewHolder.tvItemCount.text.toString().toInt() == 1) {
                dataSet.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, dataSet.size)
                onButtonClick.deleteItem()
            } else {
                onButtonClick.removeItem()
            }
        }

        viewHolder.ivDeleteItem.setOnClickListener {
            dataSet.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataSet.size)
            onButtonClick.deleteItem()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    interface onButtonClickListener {
        fun addItem()
        fun removeItem()
        fun deleteItem()
    }

}