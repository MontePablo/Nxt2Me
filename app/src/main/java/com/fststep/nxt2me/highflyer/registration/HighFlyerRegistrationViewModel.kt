/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.HighFlyerRegistrationRequest
import com.fststep.nxt2me.core.data.models.IdsUploadResponse
import com.fststep.nxt2me.core.data.models.UserRegistrationRequest
import com.fststep.nxt2me.core.di.interactors.HighFlyerRegistrationUseCase
import com.fststep.nxt2me.core.di.interactors.IdsUploadUsecase
import com.fststep.nxt2me.core.di.interactors.UserRegistrationUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Created by Shubham Singh on 29/08/21.
 */
@HiltViewModel
class HighFlyerRegistrationViewModel @Inject constructor(

    private val highFlyerRegistrationUseCase: HighFlyerRegistrationUseCase,
    private val idsUploadUsecase: IdsUploadUsecase,

    ) : ViewModel() {
    
    var registrationResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var idsUploadResponse: MutableLiveData<State<IdsUploadResponse>> = MutableLiveData()


    fun registerHighFlyer(data: HighFlyerRegistrationRequest) {
        val token="Bearer "+Preferences.fetchToken()
        Log.d("TAGG",token)
        viewModelScope.launch {
            highFlyerRegistrationUseCase(token,data).collect {
                registrationResponse.value = it
            }
        }
    }
    fun idsUpload(mobile:RequestBody,licence:MultipartBody.Part,photo:MultipartBody.Part,aadhaar:MultipartBody.Part) {
        val token="Bearer "+Preferences.fetchToken()
        Log.d("TAGG",token)
        viewModelScope.launch {
            idsUploadUsecase(token,mobile, licence, photo, aadhaar).collect {
                idsUploadResponse.value = it
            }
        }
    }
}
