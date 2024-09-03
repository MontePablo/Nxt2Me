/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.data.models.SellerListResponse
import com.fststep.nxt2me.core.data.models.SellerSearchRequest
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.core.di.interactors.ProductSearchUseCase
import com.fststep.nxt2me.core.di.interactors.ProductUpdateUsecase
import com.fststep.nxt2me.core.di.interactors.ProductUploadUsecase
import com.fststep.nxt2me.core.di.interactors.SellerSearchUseCase
import com.fststep.nxt2me.core.di.interactors.productDeleteUseCase
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
class SellerViewModel @Inject constructor(
    private val sellerSearchUseCase: SellerSearchUseCase
    ) : ViewModel() {
    
    var sellerListResponse: MutableLiveData<State<SellerListResponse>> = MutableLiveData()
    fun searchSeller(data:SellerSearchRequest) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            sellerSearchUseCase(token,data).collect {
                sellerListResponse.value = it
            }
        }
    }
}
