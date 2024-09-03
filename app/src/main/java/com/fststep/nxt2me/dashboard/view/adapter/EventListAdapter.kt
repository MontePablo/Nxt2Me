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
import com.fststep.nxt2me.dashboard.model.EventInfo

class EventListAdapter(var context: Context, private val dataSet: ArrayList<EventInfo>) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivImage: AppCompatImageView = view.findViewById(R.id.ivImage)
        var tvTitle: AppCompatTextView = view.findViewById(R.id.tvTitle)
        var tvContent: AppCompatTextView = view.findViewById(R.id.tvContent)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_people_interest_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.ivImage.setImageDrawable(ContextCompat.getDrawable(context,dataSet[position].image))
        viewHolder.tvTitle.text = dataSet[position].eventName
        viewHolder.tvContent.text = dataSet[position].eventContent
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}