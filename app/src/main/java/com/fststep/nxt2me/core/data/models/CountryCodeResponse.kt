package com.fststep.nxt2me.core.data.models

import com.google.gson.annotations.SerializedName

data class CountryCodeResponse (
    val timestamp: String? = null,
    val data: List<Country>? = null,
    val errors: Any? = null
)
