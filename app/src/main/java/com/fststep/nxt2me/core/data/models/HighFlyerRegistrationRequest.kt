package com.fststep.nxt2me.core.data.models

data class HighFlyerRegistrationRequest(
    var aadhaarFileName: String? = null,
    var area: Long? = null,
    var businessName: String? = null,
    var businessNumber: String? = null,
    var categoryId: Long? = null,
    var categoryTypeMappingId: Long? = null,
    var countryId: Long? = null,
    var customerId: String? = null,
    var driverLicenseNo: String? = null,
    var emailId: String? = null,
    var highFlyerName: String? = null,
    var latitude: Double? = null,
    var licenseFileName: String? = null,
    var longitude: Double? = null,
    var mobileNo: String? = null,
    var photoFileName: String? = null,
    var subCategoryId: Long? = null
)
