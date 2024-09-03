package com.fststep.nxt2me.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.Category
import com.fststep.nxt2me.core.data.models.CategoryResponse
import com.fststep.nxt2me.core.data.models.CountryCodeResponse
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.TermsResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.dashboard.view.DashboardActivity
import com.fststep.nxt2me.databinding.ActivitySplashBinding
import com.fststep.nxt2me.login.LoginViewModel
import com.fststep.nxt2me.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shubham Singh on 26/06/21.
 */
@AndroidEntryPoint
class SplashActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val countryViewModel: CountryViewModel by viewModels()
    private val termsViewModel: TermsViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()


    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        categoryViewModel.apply {
            observe(categoryData, ::onCategoryList)
        }
        countryViewModel.apply {
            observe(countryData, ::onCountryList)
        }
        termsViewModel.apply {
            observe(fetchTermsResponse, ::onTermsList)
        }
        loginViewModel.apply {
            observe(loginData,:: onTokenRegenerate)
        }

//        if(Preferences.fetchCategories() == null) {
            categoryViewModel.getCategories()
//        }
        if(Preferences.fetchCountry() == null) {
            countryViewModel.getCountryCode()
        }
        if(Preferences.fetchTerms() == null) {
            termsViewModel.getTerms()
        }

        if(!Preferences.fetchToken().isNullOrEmpty())
            tokenRegenerate()

        Handler(Looper.getMainLooper()).postDelayed({
            if(Preferences.fetchToken().isNullOrEmpty()){
                startActivity(Intent(this, WelcomeActivity::class.java))
            }else{
                startActivity(Intent(this, DashboardActivity::class.java))
            }
            finish()
        },3000)

    }

    private fun onTokenRegenerate(state: State<LoginResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.d(this.localClassName,"state.error ontokenGenerating Failed"+state.exception.errorMessage + state.exception.errorCode)
            }

            is State.DataState -> {
                Preferences.storeToken(state.data.token!!)
            }
            else -> {}
        }
    }

    private fun tokenRegenerate() {
        val exp=(Utility.provideTokenPayload(Preferences.fetchToken())).exp.toString()+"000"
        if(exp.toLong()<=System.currentTimeMillis()){
            loginViewModel.PostLogin(LoginRequest(Preferences.fetchPassword(),Preferences.fetchUsername()))
        }
    }

    private fun onTermsList(state: State<TermsResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.i(this.localClassName, state.exception.errorMessage)
            }

            is State.DataState -> {
                CoroutineScope(Dispatchers.IO).launch {
                    Preferences.storeTerms(state.data)
                }
                Log.d(this.localClassName,"success on termsLoad")
            }
            else -> {}
        }

    }

    private fun onCountryList(state: State<CountryCodeResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.i(this.localClassName, state.exception.errorMessage)
            }

            is State.DataState -> {
                CoroutineScope(Dispatchers.IO).launch {
                    Preferences.storeCountry(state.data)
                }
                Log.d(this.localClassName,"success on countryLoad")

            }
            else -> {}
        }
    }

    private fun onCategoryList(state: State<CategoryResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.i(this.localClassName, state.exception.errorMessage)
            }

            is State.DataState -> {
                CoroutineScope(Dispatchers.IO).launch {
                    highflyerCategoryExtraction(state.data)
                    Preferences.storeCategories(state.data)
                }

                Log.d(this.localClassName,"success on categoryLoad")
            }
            else -> {}
        }
    }
    fun highflyerCategoryExtraction(categories:CategoryResponse){
        categories.data?.highFlyerCategoryList?.add(Category(999,"High-Flyers", mutableListOf()))
//        val data= listOf("Homemade food","Classes","Tailor","Beautician","Boutique","Daycare","Event Organiser","Photographer","DJ on Hire","Delivery Boy")
        val data= listOf("Homemade","Class","Tailor","Beautician","Boutique","Daycare","Event","Photographer","DJ","Delivery")
        for (category in categories.data?.categoryList!!){
              for(subcategory in category.subCategoryList!!){
                  for(d in data){
                      if(subcategory.subCategoryName!!.lowercase().startsWith(d.lowercase())){
                          subcategory.parentId=category.id
                          categories.data.highFlyerCategoryList?.get(0)?.subCategoryList?.plusAssign(subcategory)
                      }
                  }
              }
          }
        Log.d("TAG",categories.data.highFlyerCategoryList.toString())
    }



}