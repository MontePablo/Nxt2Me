package com.fststep.nxt2me.core.data.models

data class JwtTokenPayload (
    val jti: String? = null,
    val iat: Long? = null,
    val exp: Long? = null
)