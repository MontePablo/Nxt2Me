package com.fststep.nxt2me.core.data.models

data class ProductSearchRequest (
    var categoryId: Int? = null,
    var latitude: Double? = null,
    var limitedDistance: Long? = null,
    var longitude: Double? = null,
    var page: Int? = null,
    var searchText: String? = "",
    var size: Int? = null,
    var subCategoryId: Int? = null,
    var userId:String?=null
)
