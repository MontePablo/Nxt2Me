package com.fststep.nxt2me.dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.core.data.models.CreateOrderRequest
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.dashboard.view.SellerListener
import com.fststep.nxt2me.dashboard.view.fragment.SubcategoryListener
import com.fststep.nxt2me.databinding.CustomviewCategoryBinding
import com.fststep.nxt2me.databinding.CustomviewGoodsCartItemBinding

class CartGoodsAdapter(val listener:SellerListener, var data:List<Product> = mutableListOf()):RecyclerView.Adapter<CartGoodsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewGoodsCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.let {
                    it.tvMerchantItemName.text=productName
                    it.tvMerchantItemAmount.text=productDetails?.totalCost
                    Glide.with(it.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl?:"")).into(it.ivMerchantItemImage)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewGoodsCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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