package com.fststep.nxt2me.core.data.models

data class UserRegistrationRequest(
    val countryId: Long? = null,
    val customerName: String? = null,
    val emailId: String? = null,
    val mobileNo: String? = null,
    val password: String? = null,
    val referrerReferralCode: String? = null,
    val societyName: String? = null,
    val wingFlatNo: String? = null
)
