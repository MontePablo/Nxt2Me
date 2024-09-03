/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.retrofit.exception

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

/**
 * Created by Shubham Singh on 29/08/21.
 */
open class NetworkErrorException(
    val errorCode: Int = -1,
    val errorMessage: String,
    val response: String = ""
) : Exception() {
    override val message: String
        get() = localizedMessage

    override fun getLocalizedMessage(): String {
        return errorMessage
    }

    companion object {
        fun parseException(e: HttpException): NetworkErrorException {
            val body = e.response()?.errorBody()?.string()
//            Log.d("TAG",e.response()?.headers().toString()!!)
            var msg="Unknown!"

            return try { // here you can parse the error body that comes from server
                if(!body.isNullOrEmpty())
                    msg=JSONObject(JSONObject(body!!).getString("errors")).getString("message")

                Log.d("TAG",msg)
                NetworkErrorException(e.code(), msg)
            } catch (_: Exception) {
                NetworkErrorException(e.code(), "unexpected error!!ً")
            }
        }
    }
}

class AuthenticationException(authMessage: String) :
    NetworkErrorException(errorMessage = authMessage)
