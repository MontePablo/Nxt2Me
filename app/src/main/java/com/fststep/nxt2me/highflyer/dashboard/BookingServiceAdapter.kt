/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fststep.nxt2me.R
import com.fststep.nxt2me.highflyer.registration.ServiceType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Shubham Singh on 27/06/21.
 */
class BookingServiceAdapter(private var products: ArrayList<Producttt>, private var menuEditListener: MenuEditListener) : BaseProductAdapter() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            ProductMenuMode.MODE_NORMAL.value -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_add_menu_item, parent, false)
                NormalViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_edit_booking_item, parent, false)
                EditViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]

        when (holder.itemViewType) {
            ProductMenuMode.MODE_NORMAL.value -> (holder as NormalViewHolder).apply {
                productName.text = product.name
                totalPrice.text = product.getFormattedTotalCost()
                offer.text = product.getOfferText()
                if (product.imagePath.isEmpty()) {
                    productImage.setImageDrawable(
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.ic_item_placeholder
                            )
                        }
                    )
                } else {
                    context?.let {
                        Glide.with(it)
                            .load(File(product.imagePath))
                            .into(productImage)
                    }
                }

                btDelete.setOnClickListener {
                    onDeleteClicked(product)
                }
                btEdit.setOnClickListener {
                    onEditClicked(product)
                }
            }

            ProductMenuMode.MODE_EDIT.value -> (holder as EditViewHolder).apply {
                etMarketPrice.doOnTextChanged { text, start, before, count ->
                    updateTotalPrice(text.toString().toDoubleOrNull() ?: 0.0, etDiscount.text.toString().toIntOrNull() ?: 0)
                }

                etDiscount.doOnTextChanged { text, start, before, count ->
                    updateTotalPrice(etMarketPrice.text.toString().toDoubleOrNull() ?: 0.0, text.toString().toIntOrNull() ?: 0)
                }

                etProductName.setText(product.name)
                etMarketPrice.setText(product.marketPrice.toString())
                etDiscount.setText(product.discountPercentage.toString())
                currentImagePath = product.imagePath
                context?.let {
                    Glide.with(it)
                        .load(File(currentImagePath))
                        .into(ivSelectedImage)
                }

                ivCamera.visibility = View.GONE
                layoutSelectedImage.visibility = View.VISIBLE

                btUpdateProduct.setOnClickListener {
                    cleanError()
                    if (isValid()) {
                        val index = products.indexOf(product)
                        val updatedProduct = Producttt(
                            product.id,
                            name = etProductName.text?.trim().toString(),
                            description = null,
                            weight = null,
                            imagePath = currentImagePath,
                            marketPrice = etMarketPrice.text?.trim().toString().toDoubleOrNull() ?: 0.0,
                            discountPercentage = etDiscount.text?.trim().toString().toIntOrNull() ?: 0,
                            totalCost = etTotalCost.text?.trim().toString().toDoubleOrNull() ?: 0.0,
                            unit = null,
                            type = ServiceType.BOOKING.value
                        )
                        onUpdateClicked(index, updatedProduct)
                    }
                }

                ivCamera.setOnClickListener {
                    menuEditListener.showImagePicker(true, ::setSelectedImage)
                }

                ivClearImage.setOnClickListener {
                    clearSelectedImage()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getItemViewType(position: Int): Int {
        return products[position].mode.value
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: AppCompatTextView = itemView.findViewById(R.id.tvProductName)
        val totalPrice: AppCompatTextView = itemView.findViewById(R.id.tvTotalCost)
        val offer: AppCompatTextView = itemView.findViewById(R.id.tvOffer)
        val productImage: AppCompatImageView = itemView.findViewById(R.id.ivProductImage)
        val btEdit: AppCompatImageView = itemView.findViewById(R.id.ivEdit)
        val btDelete: AppCompatImageView = itemView.findViewById(R.id.ivDelete)
    }

    inner class EditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentImagePath: String = ""

        val tlProductName: TextInputLayout = itemView.findViewById(R.id.tlProductName)
        val etProductName: TextInputEditText = itemView.findViewById(R.id.etProductName)
        val tlMarketPrice: TextInputLayout = itemView.findViewById(R.id.tlMarketPrice)
        val etMarketPrice: TextInputEditText = itemView.findViewById(R.id.etMarketPrice)
        val tlDiscount: TextInputLayout = itemView.findViewById(R.id.tlDiscount)
        val etDiscount: TextInputEditText = itemView.findViewById(R.id.etDiscount)
        val tlTotalCost: TextInputLayout = itemView.findViewById(R.id.tlTotalCost)
        val etTotalCost: TextInputEditText = itemView.findViewById(R.id.etTotalCost)
        val btUpdateProduct: AppCompatTextView = itemView.findViewById(R.id.btUpdateMenu)

        val ivCamera: AppCompatImageView = itemView.findViewById(R.id.ivCamera)
        val ivSelectedImage: AppCompatImageView = itemView.findViewById(R.id.ivSelectedImage)
        val ivClearImage: AppCompatImageView = itemView.findViewById(R.id.ivClearImage)
        val layoutSelectedImage: RelativeLayout = itemView.findViewById(R.id.layourSelectedImage)

        fun updateTotalPrice(marketPrice: Double, discount: Int) {
            val discountedPrice: Double = marketPrice - (marketPrice * discount / 100)

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            etTotalCost.setText(df.format(discountedPrice))
        }

        fun isValid(): Boolean {
            cleanError()
            var result = true
            when {
                currentImagePath.isEmpty() -> {
                    result = false
                    Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show()
                }
                etProductName.text?.trim().isNullOrEmpty() -> {
                    result = false
                    tlProductName.error = context?.getString(R.string.lbl_required)
                }
                etMarketPrice.text?.trim().isNullOrEmpty() -> {
                    result = false
                    tlMarketPrice.error = context?.getString(R.string.lbl_required)
                }
                etMarketPrice.text?.trim().toString().toDouble() <= 0.0 -> {
                    result = false
                    tlMarketPrice.error = context?.getString(R.string.lbl_invalid)
                }
                etTotalCost.text?.trim().toString().toDouble() <= 0.0 -> {
                    result = false
                    tlTotalCost.error = context?.getString(R.string.lbl_invalid)
                }
            }

            return result
        }

        fun cleanError() {
            tlProductName.error = null
            tlMarketPrice.error = null
            tlTotalCost.error = null
        }

        fun setSelectedImage(path: String) {
            if (path.isEmpty()) return
            currentImagePath = path
            context?.let {
                Glide.with(it)
                    .load(File(currentImagePath))
                    .into(ivSelectedImage)
            }
            layoutSelectedImage.visibility = View.VISIBLE
            ivCamera.visibility = View.GONE
        }

        fun clearSelectedImage() {
            currentImagePath = ""
            ivSelectedImage.setImageResource(0)
            layoutSelectedImage.visibility = View.GONE
            ivCamera.visibility = View.VISIBLE
        }
    }

    private fun onDeleteClicked(product: Producttt) {
        val index = products.indexOf(product)
        menuEditListener.onItemRemove(index)
    }

    private fun onEditClicked(product: Producttt) {
        val index = products.indexOf(product)
        menuEditListener.onItemEdit(index)
    }

    private fun onUpdateClicked(index: Int, updatedProduct: Producttt) {
        menuEditListener.onItemUpdate(index, updatedProduct)
    }
}
