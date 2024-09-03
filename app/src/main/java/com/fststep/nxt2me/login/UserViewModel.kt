/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.ChangePasswordRequest
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.ForgotPasswordRequest
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.di.interactors.ChangePasswordUseCase
import com.fststep.nxt2me.core.di.interactors.ForgotPasswordUseCase
import com.fststep.nxt2me.core.di.interactors.GetUserUseCase
import com.fststep.nxt2me.core.di.interactors.UpdateUserUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val getuserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
    ) : ViewModel() {
    

    var userDataResponse: MutableLiveData<State<UserResponse>> = MutableLiveData()
    var changePasswordResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var forgotPasswordResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var updateUserResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()


    fun getUser(data:String) {
        val token="Bearer "+Preferences.fetchToken()
        Log.d("TAGG",token)
        viewModelScope.launch {
            getuserUseCase.invoke(token,data).collect {
                userDataResponse.value = it
            }
        }
    }
    fun updateUser(data: User) {
        val token="Bearer "+Preferences.fetchToken()
        val id=Preferences.fetchUser()?.id
        viewModelScope.launch {
            updateUserUseCase(token,data,id!!).collect {
                updateUserResponse.value = it
            }
        }
    }
    fun changePassword(data: ChangePasswordRequest) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            changePasswordUseCase(token,data).collect {
                changePasswordResponse.value = it
            }
        }
    }
    fun forgotPassword(data: ForgotPasswordRequest) {
        viewModelScope.launch {
            forgotPasswordUseCase(data).collect {
                forgotPasswordResponse.value = it
            }
        }
    }
}
