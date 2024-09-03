package com.fststep.nxt2me.login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.ForgotPasswordRequest
import com.fststep.nxt2me.core.data.models.LoginRequest
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.view.CommonDialogs
import com.fststep.nxt2me.dashboard.view.DashboardActivity
import com.fststep.nxt2me.databinding.ActivityLoginBinding
import com.fststep.nxt2me.databinding.DialogForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityLoginBinding
    @Inject
    lateinit var commonDialogs: CommonDialogs
    private val loginViewModel: LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loginViewModel.apply {
            observe(loginData, ::onLoginPerform)
        }
        userViewModel.apply {
            observe(userDataResponse,::onUserSaving)
            observe(forgotPasswordResponse,::onForgotPassword)
        }
        mBinding.apply {
            password.transformationMethod= PasswordTransformationMethod.getInstance()
            btnNext.setOnClickListener(View.OnClickListener {
                if(Utility.isFieldsEmpty(arrayListOf(username,password))){
                    Toast.makeText(applicationContext,
                        getString(R.string.fill_all_fields_first),Toast.LENGTH_SHORT).show()
                }else{
                    val reqData=LoginRequest(password.text.toString(), username.text.toString())
                    loginViewModel.PostLogin(reqData)
                }
            })
            btnForgotPassword.setOnClickListener{
                val binding=DialogForgotPasswordBinding.inflate(layoutInflater)
                val dialog=commonDialogs.dialogBuild(this@LoginActivity,binding)
                binding.apply {
                    cancelbtn.setOnClickListener { dialog.dismiss() }
                    continueBtn.setOnClickListener {
                        if(Utility.isFieldsEmpty(arrayListOf(emailId,mobileNo)))
                            Toast.makeText(applicationContext,"fill all fields first!",Toast.LENGTH_SHORT).show()
                        userViewModel.forgotPassword(ForgotPasswordRequest(emailId.text.toString(), mobileNo.text.toString()))
                    }
                }
            }
        }
    }

    private fun onForgotPassword(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                Toast.makeText(applicationContext,
                    getString(R.string.password_sent_on_email_kindly_check_your_email),Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    private fun onLoginPerform(state: State<LoginResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName, state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed))
            }

            is State.DataState -> {
                Preferences.storeToken(state.data.token!!)
                mBinding.apply {
                    Preferences.storeUsername(username.text.toString())
                    Preferences.storePassword(password.text.toString())
                }
                startActivity(Intent(applicationContext, DashboardActivity::class.java))
                Toast.makeText(applicationContext, getString(R.string.welcome),Toast.LENGTH_SHORT).show()
                userViewModel.getUser(Preferences.fetchToken())
                finish()
            }
            else -> {}
        }
    }
    private fun onUserSaving(state: State<UserResponse>?){
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName,"is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Log.d(this.localClassName,"state.error onUserSaving Failed"+state.exception.errorMessage + state.exception.errorCode)
            }

            is State.DataState -> {
                Preferences.storeUser(state.data.data!!)
                Log.d(this.localClassName,"sucess userSaving")
            }
            else -> {}
        }
    }
}