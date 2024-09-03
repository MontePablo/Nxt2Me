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
import com.fststep.nxt2me.dashboard.model.PromotionOffersItemInfo

class PromotionOffersListAdapter(var context: Context, private val dataSet: ArrayList<PromotionOffersItemInfo>) :
    RecyclerView.Adapter<PromotionOffersListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: AppCompatImageView = view.findViewById(R.id.ivImage)
        var tvName: AppCompatTextView = view.findViewById(R.id.tvName)
        var tvContent: AppCompatTextView = view.findViewById(R.id.tvContent)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_promotion_offers_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ivImage.setImageDrawable(ContextCompat.getDrawable(context,dataSet[position].image))
        viewHolder.tvName.text = dataSet[position].name
        viewHolder.tvContent.text = dataSet[position].content
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}