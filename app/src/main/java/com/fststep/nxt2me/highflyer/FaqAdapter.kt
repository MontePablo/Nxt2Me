package com.fststep.nxt2me.highflyer

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.Faq

/**
 * Created by Shubham Singh on 16/08/21.
 */
class FaqAdapter(private val data: ArrayList<Faq>): RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_faq, parent,false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = data[position].title
        holder.tvTitle.setTextColor(data[position].color)

        holder.tvContent.text = if(data[position].readMore) data[position].content else data[position].clipContent

        holder.cardFaq.setOnClickListener {
            data[position].readMore = !data[position].readMore

            holder.tvContent.text = if(data[position].readMore) data[position].content else data[position].clipContent
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardFaq = itemView.findViewById<CardView>(R.id.cardFaq)
        var tvTitle = itemView.findViewById<AppCompatTextView>(R.id.tvTitle)
        var tvContent= itemView.findViewById<AppCompatTextView>(R.id.tvContent)
    }
}