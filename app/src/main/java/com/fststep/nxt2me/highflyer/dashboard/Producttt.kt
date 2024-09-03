/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.os.Parcelable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import kotlinx.parcelize.Parcelize
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Shubham Singh on 22/05/21.
 */
@Parcelize
data class Producttt(
    val id: Int,
    val name: String,
    val description: String?,
    val weight: Double?,
    val unit: String?,
    val imagePath: String = "",
    val marketPrice: Double,
    val discountPercentage: Int?,
    val totalCost: Double,
    var mode: ProductMenuMode = ProductMenuMode.MODE_NORMAL,
    var type: Int
) : Parcelable {

    fun getOfferText(): SpannableString {
        if (discountPercentage == null || discountPercentage == 0) {
            return SpannableString("")
        } else {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            val originalPriceText = "\u20B9. $marketPrice"
            val discount = marketPrice * discountPercentage / 100
            val offerText = SpannableString("$originalPriceText Save \u20B9. ${df.format(discount)} ($discountPercentage%)")
            offerText.setSpan(StrikethroughSpan(), 0, originalPriceText.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

            return offerText
        }
    }

    fun getFormattedTotalCost(): String {
        return "\u20B9. $totalCost"
    }
}

enum class ProductMenuMode(val value: Int) {
    MODE_NORMAL(0),
    MODE_EDIT(1)
}
