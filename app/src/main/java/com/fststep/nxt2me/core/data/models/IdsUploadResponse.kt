package com.fststep.nxt2me.core.data.models

data class IdsUploadResponse(
    val timestamp: String? = null,
    val data: IdsDetail? = null,
    val errors: Any? = null
)

data class IdsDetail (
    val licence: String? = null,
    val photo: String? = null,
    val aadhaar: String? = null
)