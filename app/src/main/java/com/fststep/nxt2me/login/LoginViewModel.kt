/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.di.interactors.LoginUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    var loginData: MutableLiveData<State<LoginResponse>> = MutableLiveData()

    fun PostLogin(regData: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.invoke(regData).collect {
                loginData.value = it
            }
        }
    }

}
