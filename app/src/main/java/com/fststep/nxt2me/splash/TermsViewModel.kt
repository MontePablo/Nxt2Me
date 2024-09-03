/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.TermsResponse
import com.fststep.nxt2me.core.di.interactors.AcceptTermsUseCase
import com.fststep.nxt2me.core.di.interactors.GetTermsUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shubham Singh on 29/08/21.
 */
@HiltViewModel
class TermsViewModel @Inject constructor(

    private val acceptTermsUseCase: AcceptTermsUseCase,
    private val getTermsUseCase: GetTermsUseCase

    ) : ViewModel() {
    
    var acceptTermsResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var fetchTermsResponse: MutableLiveData<State<TermsResponse>> = MutableLiveData()


    fun acceptTerms(userType:String,id:String) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            acceptTermsUseCase(token,userType,id).collect {
                acceptTermsResponse.value = it
            }
        }
    }
    fun getTerms() {
        viewModelScope.launch {
            getTermsUseCase().collect {
                fetchTermsResponse.value = it
            }
        }
    }
}
