/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.di.interactors

import android.util.Log
import com.fststep.nxt2me.core.apiservice.ApiService
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.core.data.models.UserRegistrationRequest
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.retrofit.NetworkUtil
import com.fststep.nxt2me.core.retrofit.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class productDeleteUseCase @Inject constructor(private val apiService: ApiService) {

    operator fun invoke(token:String,id:String) = flow<State<CommonResponse>> {

        apiService.productDelete(token,id).run {
            emit(State.DataState(this))
        }
    }.catch {
        it.printStackTrace()
        emit(NetworkUtil.resolveError(it))
    }.onStart {
        emit(State.LoadingState(true))
    }.onCompletion {
        emit(State.LoadingState(false))
    }.flowOn(Dispatchers.IO)
}
