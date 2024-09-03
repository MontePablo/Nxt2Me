package com.fststep.nxt2me.core.data.models

import com.google.gson.annotations.SerializedName

data class UserResponse (
    val timestamp: String? = null,
    @SerializedName("data") val data: User? = null,
    val errors: Any? = null
)




