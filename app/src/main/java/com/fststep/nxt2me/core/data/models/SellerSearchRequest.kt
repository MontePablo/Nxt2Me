package com.fststep.nxt2me.core.data.models

data class SellerSearchRequest (
    var categoryId: Long? = null,
    var latitude: Double? = null,
    var limitedDistance: Long? = null,
    var longitude: Double? = null,
    var page: Long? = null,
    var searchText: String? = "",
    var size: Long? = null,
    var subCategoryId: Long? = null,
)