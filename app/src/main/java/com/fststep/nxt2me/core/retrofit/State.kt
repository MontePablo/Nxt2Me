/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.retrofit

import com.fststep.nxt2me.core.retrofit.exception.NetworkErrorException

sealed class State<out T : Any> {
    data class LoadingState(val isLoading: Boolean) : State<Nothing>()
    data class ErrorState(var exception: NetworkErrorException) : State<Nothing>()
    data class DataState<T : Any>(var data: T) : State<T>()
}
