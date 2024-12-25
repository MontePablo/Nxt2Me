package com.fststep.nxt2me.core.data.models

data class CreateOrderRequest (
    val bookingDate: String? = null,
    val bookingSlotId: Long? = null,
    val customerId: String? = null,
    val highFlyerId: String? = null,
    val isVisitorService: Boolean? = null,
    val merchantId: String? = null,
    var products: MutableList<Product>? = null,
    val totalAmount: Long? = null
)
