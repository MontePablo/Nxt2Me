package com.fststep.nxt2me.core.data.models

data class ChangePasswordRequest (
    val id: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null
)
