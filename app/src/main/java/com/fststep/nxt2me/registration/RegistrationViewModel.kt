/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.UserRegistrationRequest
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.di.interactors.UserRegistrationUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shubham Singh on 29/08/21.
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRegistrationUseCase: UserRegistrationUseCase,
) : ViewModel() {

    var registrationResponse: MutableLiveData<State<UserResponse>> = MutableLiveData()

    fun registerUser(userData: UserRegistrationRequest) {
        viewModelScope.launch {
            userRegistrationUseCase(userData).collect {
                registrationResponse.value = it
            }
        }
    }
}
