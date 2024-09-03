package com.fststep.nxt2me.core.data.models

data class ProductRequest(
    var name: String? = null,
    var productDetails: ProductDetail? = null,
    var isHighFlyer: Boolean? = null,
    var productTypeId: Long? = null,
    var userId: String? = null
)
data class ProductDetail (
    var perPercelKm: String? = null,
    var price: String? = null,
    var internetCharge:String?=null,
    var totalCost:String?=null,
    var discount:String?=null,
    var desc:String?=null,
    var rentOrSale:String?=null,
    var quantity:String?=null
)

