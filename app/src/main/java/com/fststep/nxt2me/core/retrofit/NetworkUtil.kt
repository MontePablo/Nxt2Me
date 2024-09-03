/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.retrofit

import com.fststep.nxt2me.core.retrofit.exception.NetworkErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Shubham Singh on 29/08/21.
 */
class NetworkUtil {
    companion object {
        fun resolveError(e: Throwable): State.ErrorState {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "connection error!")
                }
                is ConnectException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
            }

            if (e is HttpException) {
                when (e.code()) {
//                    502 -> {
//                        error = NetworkErrorException(e.code(), "internal error!")
//                    }
//                    401 -> {
////                        throw AuthenticationException("authentication error!")
//                    }
//                    400 -> {
//                        error = NetworkErrorException.parseException(e)
//                    }
                    else -> error=NetworkErrorException.parseException(e)
                }
            }

            return State.ErrorState(error as NetworkErrorException)
        }
    }
}
class AuthenticationException(authMessage: String) :
    NetworkErrorException(errorMessage = authMessage)
