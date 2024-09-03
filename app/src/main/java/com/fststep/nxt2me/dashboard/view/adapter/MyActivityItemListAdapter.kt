package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.MyActivityItemInfo
import com.fststep.nxt2me.dashboard.view.MyActivityItemDetailsActivity

class MyActivityItemListAdapter(var context: Context, private val dataSet: ArrayList<MyActivityItemInfo>) :
    RecyclerView.Adapter<MyActivityItemListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDate: AppCompatTextView = view.findViewById(R.id.tvDate)
        var tvTitle: AppCompatTextView = view.findViewById(R.id.tvTitle)
        var tvAmount: AppCompatTextView = view.findViewById(R.id.tvAmount)
        var clItem: ConstraintLayout = view.findViewById(R.id.clItem)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_my_activity_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvDate.text = dataSet[position].date
        viewHolder.tvTitle.text = dataSet[position].title
        viewHolder.tvAmount.text = "₹ " + dataSet[position].amount + "/-"

        if (dataSet[position].amount == "") {
            viewHolder.tvAmount.visibility = View.GONE
        }

        viewHolder.clItem.setOnClickListener {
            val intent = Intent(it.context,MyActivityItemDetailsActivity::class.java).apply {
                putExtra("status",dataSet[position].status)
            }
            it.context.startActivity(intent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}