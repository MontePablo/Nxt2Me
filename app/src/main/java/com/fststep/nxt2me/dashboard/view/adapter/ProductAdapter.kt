package com.fststep.nxt2me.dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.extension.getImageDownloadUrl

import com.fststep.nxt2me.dashboard.view.ProductListener
import com.fststep.nxt2me.databinding.CustomviewBookingItemBinding
import com.fststep.nxt2me.databinding.CustomviewGoodsItemBinding
import com.fststep.nxt2me.databinding.CustomviewPhoneItemBinding

class ProductAdapter(
    val context: Context,
    var type: CategoryTypeEnum,
    val listener: ProductListener,
    var data: List<Product> = listOf(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderGoods(val binding: CustomviewGoodsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.run {
                    tvMerchantItemName.text =productName
                    tvMerchantItemAmount.text=productDetails?.totalCost
                    Glide.with(this.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl?:"")).into(this.ivMerchantItemImage)
                    btnAddToCart.setOnClickListener { btnAddToCart.text=
                        context.getString(R.string.remove_from_cart) }
                }
            }
        }
    }

    // ViewHolder for the second layout
    inner class ViewHolderBooking(val binding: CustomviewBookingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.run {
                    tvMerchantItemName.text =productName
                    tvMerchantItemAmount.text=productDetails?.totalCost
                    Glide.with(this.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl?:"")).into(this.ivMerchantItemImage)

                }
            }
        }
    }
    inner class ViewHolderPhone(val binding: CustomviewPhoneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(position: Int) {
            data[position].apply {
                binding.run {
                    tvMerchantItemName.text =productName
                    tvMerchantItemAmount.text=productDetails?.price
                    Glide.with(this.ivMerchantItemImage.context).load(getImageDownloadUrl(productImageUrl?:"")).into(this.ivMerchantItemImage)
                    ivCall.setOnClickListener { Toast.makeText(context,"Not yet implemented",Toast.LENGTH_SHORT).show() }
                    ivChat.setOnClickListener { Toast.makeText(context,"Not yet implemented",Toast.LENGTH_SHORT).show() }

                }
            }
        }
    }
//    inner class ViewHolderDelivery(val binding: SecondBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bindItem(position: Int) {
//            data.sudent_arr!![position].apply {
//                binding.anotherTextView.text = user_name // Adjust for your second layout
//                binding.anotherRank.text = rank.toString() // Adjust for your second layout
//            }
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            CategoryTypeEnum.Good -> {
                val binding = CustomviewGoodsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderGoods(binding)
            }
            CategoryTypeEnum.PhoneNumber-> {
                val binding = CustomviewPhoneItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderPhone(binding)
            }
            CategoryTypeEnum.Booking -> {
                val binding = CustomviewBookingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderBooking(binding)
            }
//            CategoryTypeEnum.Delivery -> {
//                val binding = SecondBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//                ViewHolderPhone(binding)
//            }
            CategoryTypeEnum.Property-> {
                val binding = CustomviewPhoneItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderPhone(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderGoods -> holder.bindItem(position)
            is ViewHolderPhone -> holder.bindItem(position)
            is ViewHolderBooking -> holder.bindItem(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun updateData(data:List<Product>){
        this.data=data
        notifyDataSetChanged()
    }
}
