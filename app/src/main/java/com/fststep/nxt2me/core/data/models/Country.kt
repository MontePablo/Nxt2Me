package com.fststep.nxt2me.core.data.models

data class Country (
    val id: Long? = null,
    val countryName: String? = null,
    val countryCode: String? = null
){
    override fun toString(): String {
        return "$countryName $countryCode"
    }
}