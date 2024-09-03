package com.fststep.nxt2me.dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.dashboard.view.SellerListener
import com.fststep.nxt2me.dashboard.view.fragment.SubcategoryListener
import com.fststep.nxt2me.databinding.CustomviewCategoryBinding
import com.fststep.nxt2me.databinding.CustomviewSellerBinding

class SellerAdapter(val listener:SellerListener, var data:List<Seller> = mutableListOf()):RecyclerView.Adapter<SellerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewSellerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.let {
                    it.tvMerchantName.setText(this.name)
                    it.rootView.setOnClickListener{listener.onSellerClick(data[position])}
//                    Glide.with(it.ivMerchantImage.context).load(getImageDownloadUrl(this.)).into(it.ivMerchantImage)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }
    fun updateData(data:List<Seller>){
        this.data=data
        notifyDataSetChanged()
    }

}