package com.fststep.nxt2me.core.data.models


data class User (
    var id: String? = null,
    var customerName: String? = null,
    var mobileNo: String? = null,
    var countryId: Long? = null,
    var emailId: String? = null,
    var societyName: String? = null,
    var wingFlatNo: String? = null,
    var referralCode: String? = null,
    var referrerReferralCode: String? = null,
    var highFlyerDto: HighFlyerDto? = null,
    var isTCAccepted: Boolean? = null
)