package com.fststep.nxt2me.core.data.models
data class SellerListResponse (
    val timestamp: String? = null,
    val data: SellerListData? = null,
    val errors: Any? = null
)

data class SellerListData (
    val content: List<Seller>? = null,
    val pageable: Pageable? = null,
    val totalElements: Long? = null,
    val totalPages: Long? = null,
    val last: Boolean? = null,
    val numberOfElements: Long? = null,
    val first: Boolean? = null,
    val sort: Sort? = null,
    val size: Long? = null,
    val number: Long? = null,
    val empty: Boolean? = null
)

data class Seller (
    val id: String? = null,
    val actualDistance: Long? = null,
    val name: String? = null,
    val mobileNo: String? = null,
    val countryId: Long? = null,
    val emailId: String? = null,
    val businessName: String? = null,
    val area: Long? = null,
    val highFlyer: Boolean? = null,
    val enableDistanceMsg: Boolean? = null,
    val categoryId: Long? = null,
    val subCategoryId: Long? = null
)