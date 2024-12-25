package com.fststep.nxt2me.core.data.models

data class ProductListResponse (
    val timestamp: String? = null,
    val data: ProductListData? = null,
    val errors: Any? = null
)

data class ProductListData (
    val content: List<Product>? = null,
    val pageable: Pageable? = null,
    val last: Boolean? = null,
    val totalPages: Long? = null,
    val totalElements: Long? = null,
    val size: Long? = null,
    val number: Long? = null,
    val sort: Sort? = null,
    val first: Boolean? = null,
    val numberOfElements: Long? = null,
    val empty: Boolean? = null
)

data class Product (
    val productId: String? = null,
    val actualDistance: Long? = null,
    val productName: String? = null,
    val productDetails: ProductDetail? = null,
    val productImageUrl: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val mobileNo: String? = null,
    val countryId: Long? = null,
    val emailId: String? = null,
    val businessName: String? = null,
    val area: Long? = null,
    val highFlyer: Boolean? = null,
    val enableDistanceMsg: Boolean? = null,
    val categoryId: Long? = null,
    val subCategoryId: Long? = null,

    var isInCart: Boolean =false, //offline
    var quantity: Int =1 // offline

)
data class Pageable (
    val sort: Sort? = null,
    val offset: Long? = null,
    val pageSize: Long? = null,
    val pageNumber: Long? = null,
    val paged: Boolean? = null,
    val unpaged: Boolean? = null
)

data class Sort (
    val empty: Boolean? = null,
    val unsorted: Boolean? = null,
    val sorted: Boolean? = null
)