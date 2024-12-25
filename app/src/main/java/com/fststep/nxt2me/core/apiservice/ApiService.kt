/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.core.apiservice

import com.fststep.nxt2me.core.data.models.CategoryResponse
import com.fststep.nxt2me.core.data.models.ChangePasswordRequest
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.CountryCodeResponse
import com.fststep.nxt2me.core.data.models.CreateOrderRequest
import com.fststep.nxt2me.core.data.models.ForgotPasswordRequest
import com.fststep.nxt2me.core.data.models.HighFlyerRegistrationRequest
import com.fststep.nxt2me.core.data.models.IdsUploadResponse
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.data.models.SellerListResponse
import com.fststep.nxt2me.core.data.models.SellerSearchRequest
import com.fststep.nxt2me.core.data.models.TermsResponse
import com.fststep.nxt2me.core.data.models.User
import com.fststep.nxt2me.core.data.models.UserRegistrationRequest
import com.fststep.nxt2me.core.data.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("fst-registration/data/api/category/all")
    suspend fun getCategories(): CategoryResponse
    @GET("fst-registration/data/api/country/all")
    suspend fun getCountryCode(): CountryCodeResponse
    @GET("fst-registration/data/api/termsCondition/all")
    suspend fun getTerms():TermsResponse
    @PUT("fst-registration/api/updateTerms/{userType}/{id}")
    suspend fun acceptTerms(@Header("Authorization") token:String,@Path("userType") userType:String,@Path("id") id:String): CommonResponse
    @POST("fst-registration/api/customer")
    suspend fun registerUser(@Body request: UserRegistrationRequest): UserResponse
    @PUT("fst-registration/api/customer/{id}")
    suspend fun updateUser(@Header("Authorization") token:String, @Body request: User, @Path("id") id:String): CommonResponse
    @GET("fst-registration/api/customer/identity")
    suspend fun getUser(@Header("Authorization") token:String, @Query("token") data:String):UserResponse
    @POST("customer/login")
    suspend fun performLogin(@Body request: LoginRequest):LoginResponse
    @POST("fst-registration/api/highflyer")
    suspend fun registerHighFlyer(@Header("Authorization") token:String,@Body request: HighFlyerRegistrationRequest): CommonResponse
    @POST("fst-registration/api/customer/changePassword")
    suspend fun changePassword(@Header("Authorization") token:String,@Body request: ChangePasswordRequest): CommonResponse
    @POST("fst-registration/api/customer/forgotPassword")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): CommonResponse

    @Multipart
    @POST("fst-registration/api/documents/upload")
    suspend fun idsUpload(@Header("Authorization") token:String,@Part("Mobile Number") mobile:RequestBody,@Part licence:MultipartBody.Part,@Part photo:MultipartBody.Part,@Part aadhaar:MultipartBody.Part):IdsUploadResponse


    @Multipart
    @POST("fst-product/api/addProduct")
    suspend fun productUpload(@Header("Authorization") token:String,@Part("jsonString") jsonString:RequestBody):CommonResponse
    @Multipart
    @POST("fst-product/api/addProduct")
    suspend fun productUpload(@Header("Authorization") token:String,@Part("jsonString") jsonString:RequestBody,@Part fileToUpload:MultipartBody.Part):CommonResponse
    @Multipart
    @PUT("fst-product/api/product/{id}")
    suspend fun productUpdate(@Header("Authorization") token:String,@Path("id") id:String,@Part("jsonString") jsonString:RequestBody,@Part fileToUpload:MultipartBody.Part):CommonResponse
    @Multipart
    @PUT("fst-product/api/product/{id}")
    suspend fun productUpdate(@Header("Authorization") token:String,@Path("id") id:String,@Part("jsonString") jsonString:RequestBody):CommonResponse

    @POST("fst-search/api/searchProductByLocation")
    suspend fun productSearch(@Header("Authorization") token:String,@Body data:ProductSearchRequest):ProductListResponse

    @DELETE("fst-product/api/product/delete/{id}")
    suspend fun productDelete(@Header("Authorization") token:String, @Path("id") id:String):CommonResponse

    @POST("fst-search/api/searchMerchantByLocation")
    suspend fun sellerSearch(@Header("Authorization") token:String,@Body data:SellerSearchRequest):SellerListResponse

    @POST("fst-order/api/createOrder")
    suspend fun createOrder(@Header("Authorization") token:String,@Body data:CreateOrderRequest):CommonResponse
}
