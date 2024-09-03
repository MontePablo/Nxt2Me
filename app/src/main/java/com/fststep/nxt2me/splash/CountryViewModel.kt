/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.models.CategoryResponse
import com.fststep.nxt2me.core.data.models.CountryCodeResponse
import com.fststep.nxt2me.core.di.interactors.GetCategoriesUseCase
import com.fststep.nxt2me.core.di.interactors.GetCountryCodeUseCase
import com.fststep.nxt2me.core.retrofit.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountryCodeUseCase: GetCountryCodeUseCase
) : ViewModel() {

    var countryData: MutableLiveData<State<CountryCodeResponse>> = MutableLiveData()

    fun getCountryCode() {
        viewModelScope.launch {
            getCountryCodeUseCase().collect {
                countryData.value = it
            }
        }
    }
}
