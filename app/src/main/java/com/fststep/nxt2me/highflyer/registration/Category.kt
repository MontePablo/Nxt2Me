package com.fststep.nxt2me.highflyer.registration

import com.fststep.nxt2me.core.data.models.SubCategory

/**
 * Created by Shubham Singh on 01/08/21.
 */
data class Category(
    val categoryId: Int,
    val categoryName: String,
    val subCategories: ArrayList<SubCategory>
)