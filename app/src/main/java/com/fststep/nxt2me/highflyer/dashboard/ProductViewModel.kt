/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.core.di.interactors.ProductSearchUseCase
import com.fststep.nxt2me.core.di.interactors.ProductUpdateUsecase
import com.fststep.nxt2me.core.di.interactors.ProductUploadUsecase
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
class ProductViewModel @Inject constructor(
    private val productUploadUsecase: ProductUploadUsecase,
    private val productUpdateUsecase: ProductUpdateUsecase,
    private val productSearchUseCase: ProductSearchUseCase,
    private val productDeleteUseCase: productDeleteUseCase
    ) : ViewModel() {
    
    var productUploadResponse: MutableLiveData<State<CommonResponse>> = MutableLiveData()
    var productListResponse:MutableLiveData<State<ProductListResponse>> = MutableLiveData()
    var productDeleteResponse:MutableLiveData<State<CommonResponse>> = MutableLiveData()
    fun addProduct(jsonString:RequestBody,fileToUpload: MultipartBody.Part?=null) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            productUploadUsecase(token, jsonString,fileToUpload).collect {
                productUploadResponse.value = it
            }
        }
    }
    fun updateProduct(id:String,jsonString:RequestBody,fileToUpload: MultipartBody.Part?=null) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            productUpdateUsecase(token,id, jsonString,fileToUpload).collect {
                productUploadResponse.value = it
            }
        }
    }
    fun searchProduct(data:ProductSearchRequest) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            productSearchUseCase(token,data).collect {
                productListResponse.value = it
            }
        }
    }
    fun deleteProduct(id: String) {
        val token="Bearer "+Preferences.fetchToken()
        viewModelScope.launch {
            productDeleteUseCase(token,id).collect {
                productDeleteResponse.value = it
            }
        }
    }
}
