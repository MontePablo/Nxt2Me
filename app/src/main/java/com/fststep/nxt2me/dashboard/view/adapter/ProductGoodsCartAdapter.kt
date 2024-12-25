package com.fststep.nxt2me.dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.dashboard.view.GoodsCartListener
import com.fststep.nxt2me.dashboard.view.SellerListener
import com.fststep.nxt2me.databinding.CustomviewGoodsCartItemBinding

class ProductGoodsCartAdapter(val listener:GoodsCartListener, var data:List<Product> = mutableListOf()):RecyclerView.Adapter<ProductGoodsCartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewGoodsCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.let {
                    Glide.with(it.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl!!)).into(it.ivMerchantItemImage)
                    it.tvItemCount.text=this.quantity.toString()
                    it.tvMerchantItemAmount.text=this.productDetails?.totalCost
                    it.tvMinus.setOnClickListener{listener.decreaseQuantity(data[position]);notifyItemChanged(position)}
                    it.tvAdd.setOnClickListener{listener.increaseQuantity(data[position]);notifyItemChanged(position)}
                    it.ivDeleteItem.setOnClickListener{listener.deleteProduct(data[position]);notifyItemChanged(position)}
                    it.tvMerchantItemName.text=this.productName
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