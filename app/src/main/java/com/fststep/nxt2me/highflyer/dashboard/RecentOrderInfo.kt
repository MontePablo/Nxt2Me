/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

/**
 * Created by Jay Kulshreshtha on 22/05/21.
 */
data class RecentOrderInfo(
    var orderAddress: String,
    var orderType: String,
    var orderDate: String,
    var orderTime: String,
    var orderStatus: String,
    var useDeliveryPerson: Boolean = false,
    var deliveryPerson: DeliveryPerson? = null
)

data class DeliveryPerson(
    var id: Int,
    var name: String,
    var contact: String,
    var charge: String
)
