package com.fststep.nxt2me.highflyer.registration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Shubham Singh on 01/08/21.
 */
@Parcelize
data class HighFlyer (
    var id: Int,
    var name: String,
    var mobileNumber: String,
    var businessName: String,
    var businessCategory: String,
    var typeOfBusiness: String?,
    var serviceType: Int,
    var deliveryType: String? = null,
    var servicePlace: String? = null,
    var businessNumber: String? = null,
    val deliveryGender: String? = null,
    var area: Double,
    var deliveryLicenseNumber: String? = null
) : Parcelable