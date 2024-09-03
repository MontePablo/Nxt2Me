package com.fststep.nxt2me.core.di.modules

import android.content.Context
import com.fststep.nxt2me.BuildConfig
import com.fststep.nxt2me.core.apiservice.ApiService
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Shubham Singh on 26/06/21.
 */

@Module
@InstallIn(SingletonComponent::class)
object SingletonModules {

//    @Singleton
//    @Provides
//    fun providesPreferences(@ApplicationContext context: Context): Preferences {
//        return Preferences(context)
//    }

    @Singleton
    @Provides
    fun providesApiService(httpLoggingInterceptor: HttpLoggingInterceptor): ApiService {

        val builder = OkHttpClient.Builder()
        builder.interceptors().apply {
            add(httpLoggingInterceptor)
        }
        builder.readTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(Constants.NETWORK_CALL_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesHttpLoginInterceptor(): HttpLoggingInterceptor {
        val interceptorLog = HttpLoggingInterceptor()
        interceptorLog.level = if (BuildConfig.BUILD_TYPE.equals("debug", ignoreCase = true))
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        return interceptorLog
    }
}