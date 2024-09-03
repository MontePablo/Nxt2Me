package com.fststep.nxt2me.core.di.interactors

import com.fststep.nxt2me.core.apiservice.ApiService
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.HighFlyerRegistrationRequest
import com.fststep.nxt2me.core.data.models.IdsUploadResponse
import com.fststep.nxt2me.core.retrofit.NetworkUtil
import com.fststep.nxt2me.core.retrofit.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class IdsUploadUsecase@Inject constructor(private val apiService: ApiService) {
    operator fun invoke(token:String, mobile:RequestBody,licence: MultipartBody.Part,photo: MultipartBody.Part,aadhaar: MultipartBody.Part) = flow<State<IdsUploadResponse>> {
        apiService.idsUpload(token,mobile,licence,photo,aadhaar).run {
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