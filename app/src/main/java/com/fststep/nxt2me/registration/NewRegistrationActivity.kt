package com.fststep.nxt2me.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.UserRegistrationRequest
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.view.CommonDialogs
import com.fststep.nxt2me.databinding.ActivityNewRegistrationBinding
import com.fststep.nxt2me.login.LoginViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Jay Kulshreshtha on 26/06/21.
 */
@AndroidEntryPoint
class NewRegistrationActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityNewRegistrationBinding
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    val dialogs=CommonDialogs()
    private lateinit var newRegistrationViewPagerAdapter: NewRegistrationViewPagerAdapter
    private val registrationViewModel: RegistrationViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    var username=""
    var password=""
    var countryId=1L
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_registration)
        registrationViewModel.apply {
            observe(registrationResponse, ::onRegistrationPerform)
        }
        loginViewModel.apply {
            observe(loginData,::onLogin)
        }
        mBinding.apply {
            btnNext.setOnClickListener(View.OnClickListener {
                val v= Utility.isFieldsEmpty(arrayListOf<EditText>(etName,etMail,etMobileNumber,etSocietyName,etPassword,etConfirmPassword,etWing))
                if(v)
                    Toast.makeText(applicationContext,"fill all fields first!",Toast.LENGTH_SHORT).show()
                else if(etPassword.text.toString()!=etConfirmPassword.text.toString()){
                    Toast.makeText(applicationContext,"password missmatch!",Toast.LENGTH_SHORT).show()
                }else{
                    username=etName.text.toString();password=etPassword.text.toString()
                    val reqData= UserRegistrationRequest(countryId,etName.text.toString(),etMail.text.toString(),etMobileNumber.text.toString(),etPassword.text.toString(),etReferralCode.text.toString(),etSocietyName.text.toString(),etWing.text.toString())
                    registrationViewModel.registerUser(reqData)
                    Log.d("TAG", Gson().toJson(reqData).toString())
                }
            })

            spinnerCountryCode.apply {
                val countryCodes=Preferences.fetchCountry()?.data?: listOf()
                adapter=ArrayAdapter(this@NewRegistrationActivity,R.layout.support_simple_spinner_dropdown_item,countryCodes)
                onItemSelectedListener=object: OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        countryId=countryCodes[position].id?:0
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(this@NewRegistrationActivity,"choose country code first",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        tabLayout = mBinding.tlDots
        viewPager = mBinding.vpContent
        tabLayout!!.setupWithViewPager(viewPager)

        setupViewPager(viewPager!!)
        setupTabIcons()
    }

    private fun setupViewPager(viewpager: ViewPager) {
        newRegistrationViewPagerAdapter = NewRegistrationViewPagerAdapter(supportFragmentManager)

        newRegistrationViewPagerAdapter.addFragment(FirstPaginationContentFragment())
        newRegistrationViewPagerAdapter.addFragment(SecondPaginationContentFragment())
        newRegistrationViewPagerAdapter.addFragment(ThirdPaginationContentFragment())
        newRegistrationViewPagerAdapter.addFragment(FourthPaginationContentFragment())
        newRegistrationViewPagerAdapter.addFragment(FifthPaginationContentFragment())

        // setting adapter to view pager.
        viewpager.adapter = newRegistrationViewPagerAdapter
    }

    private fun setupTabIcons() {
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    private fun onRegistrationPerform(state: State<UserResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                Toast.makeText(applicationContext,
                    getString(R.string.success),Toast.LENGTH_SHORT).show()
                Preferences.storeUser(state.data.data!!)
                loginViewModel.PostLogin(LoginRequest(password, username))

            }
            else -> {}
        }
    }
    private fun onLogin(state: State<LoginResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.d(this.localClassName,"signup complete\nbut login failed: "+state.exception.errorMessage)
                Utility.performErrorState(this,state, getString(R.string.login_failed)+"\n"+state.exception.errorMessage)
            }

            is State.DataState -> {
                Preferences.storeToken(state.data.token!!)
                startActivity(Intent(applicationContext,RegistrationTnCActivity::class.java))
                finishAffinity()
            }
            else -> {}
        }
    }

}