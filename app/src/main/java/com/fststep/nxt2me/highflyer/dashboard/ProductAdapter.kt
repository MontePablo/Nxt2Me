package com.fststep.nxt2me.highflyer.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.core.util.Utility.discountAmount
import com.fststep.nxt2me.databinding.CustomviewItemsHighflyerBinding

class ProductAdapter(val context: Context, val listener: ProductButtonListener, var data: List<Product> = listOf()):RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:CustomviewItemsHighflyerBinding):RecyclerView.ViewHolder(binding.root){
        fun bindItem(position:Int) {
            data[position].apply {
                binding.apply {
                    this.tvProductName.text=productName
                    this.ivDelete.setOnClickListener { listener.delete(position,data[position]) }
                    this.ivEdit.setOnClickListener { listener.edit(position,data[position]) }
                    this.tvDescription.text=productDetails?.desc?:""
                    this.btnLayout.visibility= View.VISIBLE
                    if(!productDetails?.totalCost.isNullOrEmpty()){
                        tvTotalCost.apply {
                            visibility=View.VISIBLE
                            text="₹. ${productDetails?.totalCost}"
                        }
                    }
                    if(!productDetails?.rentOrSale.isNullOrEmpty()){
                        tvOffer.apply {
                            visibility=View.VISIBLE
                            text=productDetails?.rentOrSale
                        }
                    }
                    if(!productDetails?.desc.isNullOrEmpty()){
                        tvDescription.apply {
                            visibility=View.VISIBLE
                            text= productDetails?.desc
                        }
                    }
                    if(!productDetails?.price.isNullOrEmpty()){
                        tvOffer.apply {
                            visibility=View.VISIBLE
                            text="₹. ${productDetails?.price}"
                        }
                    }
                    if(!productDetails?.discount.isNullOrEmpty()){
                        tvOffer.apply {
                            visibility=View.VISIBLE
                            val discountPrice= discountAmount(productDetails?.discount?.toDouble()
                                ?:0.0,productDetails?.price?.toDouble()?:0.0)
                            text= "${this.text}Save ₹.$discountPrice(${productDetails?.discount})"
                        }
                    }
                    Glide.with(this.ivProductImage.context).load(getImageDownloadUrl(productImageUrl?:"")).into(this.ivProductImage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewItemsHighflyerBinding.inflate(LayoutInflater.from(parent.context)))
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

interface ProductButtonListener{
    fun edit(position:Int, product:Product)
    fun delete(position:Int, product:Product)
}