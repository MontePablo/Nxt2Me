package com.fststep.nxt2me.core.retrofit.exception

/**
 * Created by Shubham Singh on 26/06/21.
 */
data class ErrorBody (
    var code: String? = null,
    var error: String? = null,
    var message: String? = null,
    var correlationId: String? = null
)