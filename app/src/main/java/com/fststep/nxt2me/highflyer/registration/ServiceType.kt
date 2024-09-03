package com.fststep.nxt2me.highflyer.registration

enum class ServiceType(val value: Int) {
    GOODS_DELIVERY(1),
    PHONE(2),
    BOOKING(3),
    DELIVERY(4),
    NONE(0);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}