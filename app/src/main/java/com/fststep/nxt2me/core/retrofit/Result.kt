package com.fststep.nxt2me.core.retrofit

import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.retrofit.exception.Failure

/**
 * Created by Shubham Singh on 26/06/21.
 */

sealed class Result<out T: Any?>

class Success<out T: Any?>(val data: T): Result<T>()

class Error(
    val exception: Throwable? = null,
    val failure: Failure = Failure.UnKnownDataLoadException,
    val message: String = exception?.message ?: Constants.UnknownError
): Result<Nothing>()

class Progress(val isLoading: Boolean): Result<Nothing>()