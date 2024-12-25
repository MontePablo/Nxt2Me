package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.dashboard.view.GoodsCartListener
import com.fststep.nxt2me.dashboard.view.SellerListener
import com.fststep.nxt2me.databinding.CustomviewBookingCartItemBinding
import com.fststep.nxt2me.databinding.CustomviewGoodsCartItemBinding

class ProductBookingCartAdapter(context:Context,var data:List<Product> = mutableListOf()):RecyclerView.Adapter<ProductBookingCartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewBookingCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.let {
                    Glide.with(it.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl!!)).into(it.ivMerchantItemImage)
                    it.tvMerchantItemAmount.text=this.productDetails?.totalCost
                    it.tvMerchantItemName.text=this.productName
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewBookingCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(position)
    }
    fun updateData(data:List<Product>){
        this.data=data
        notifyDataSetChanged()
    }

}