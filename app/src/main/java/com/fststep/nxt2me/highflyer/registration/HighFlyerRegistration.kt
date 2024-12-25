package com.fststep.nxt2me.highflyer.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.util.Util
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.HighFlyerRegistrationRequest
import com.fststep.nxt2me.core.data.models.IdsDetail
import com.fststep.nxt2me.core.data.models.IdsUploadResponse
import com.fststep.nxt2me.core.data.models.LoginResponse
import com.fststep.nxt2me.core.data.models.SubCategory
import com.fststep.nxt2me.core.data.models.UserResponse
import com.fststep.nxt2me.core.extension.multiPartBodyFromFile
import com.fststep.nxt2me.core.extension.multiPartBodyFromUri
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.extension.partFromString
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.RealPathUtil
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.util.Utility.cleanError
import com.fststep.nxt2me.core.util.Utility.highlightErrorField
import com.fststep.nxt2me.databinding.ActivityHighflyerRegistrationBinding
import com.fststep.nxt2me.login.LoginViewModel
import com.fststep.nxt2me.login.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Shubham Singh on 01/08/21.
 */
@AndroidEntryPoint
class HighFlyerRegistration: AppCompatActivity() {
    private val highFlyerRegistrationViewModel: HighFlyerRegistrationViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var mBinding: ActivityHighflyerRegistrationBinding
    

     var selectedCategoryId=0L
     var selectedSubCategoryId=0L
     lateinit var selectedSubCategory:SubCategory
     var isIdUploaded=false
    lateinit var contract:ActivityResultLauncher<String>
    val aadharFile=PickedFileData()
    val licenceFile=PickedFileData()
    val photoFile=PickedFileData()
    var idsDetail: IdsDetail?=null
    var imageUploadTag=""

    inner class PickedFileData{
        var file:File?=null
        var filePart:MultipartBody.Part?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityHighflyerRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        userViewModel.apply {
            observe(userDataResponse,::onUserSaving)
        }

        contract=registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it!=null) {

                val imageUri = it
                if (imageUploadTag == Constants.AADHAAR) {
                    aadharFile.apply {
//                        file =
//                            File(RealPathUtil.getRealPath(this@HighFlyerRegistration, imageUri!!))
//                        filePart = multiPartBodyFromFile(
//                            Constants.AADHAAR,
//                            System.currentTimeMillis().toString() + ".jpg",
//                            file!!
//                        )
                    filePart= multiPartBodyFromUri(this@HighFlyerRegistration,imageUri,Constants.AADHAAR)

                    }
                    mBinding.adharSuccessMark.visibility = View.VISIBLE
                } else if (imageUploadTag == Constants.PHOTO) {
                    photoFile.apply {
                    filePart= multiPartBodyFromUri(this@HighFlyerRegistration,imageUri,Constants.PHOTO)
                    }
                    mBinding.photoSuccessMark.visibility = View.VISIBLE
                } else if (imageUploadTag == Constants.LICENCE) {
                    licenceFile.apply {
                    filePart= multiPartBodyFromUri(this@HighFlyerRegistration,imageUri,Constants.LICENCE)

                    }
                    mBinding.licenseSuccessMark.visibility = View.VISIBLE
                }
                if (aadharFile.filePart != null && photoFile.filePart != null && licenceFile.filePart != null) {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.buttonNext.visibility=View.GONE
                    highFlyerRegistrationViewModel.idsUpload(partFromString(Preferences.fetchUser()?.mobileNo!!), licenceFile.filePart!!, photoFile.filePart!!, aadharFile.filePart!!)
                    Toast.makeText(this@HighFlyerRegistration,"uploading images",Toast.LENGTH_SHORT).show()
                }
            }
        }

        highFlyerRegistrationViewModel.apply {
            observe(registrationResponse, ::onRegistrationPerform)
            observe(idsUploadResponse,::onIdUploaded)
        }

        mBinding.apply {
            toolbar.btBack.setOnClickListener { onBackPressed() }
            buttonNext.setOnClickListener {
                if (isValid()) {
                    val highFlyerData= HighFlyerRegistrationRequest().apply {
                        countryId=Preferences.fetchUser()?.countryId
                        emailId=Preferences.fetchUser()?.emailId
                        customerId=Preferences.fetchUser()?.id
                        mobileNo=etMobileNumber.text.toString()
                        businessName=etBusinessName.text.toString()
                        highFlyerName=etBusinessName.text.toString()
                        businessNumber=etBusinessNumber.text.toString()
                        categoryId= selectedCategoryId.toLong()
                        subCategoryId= selectedSubCategoryId.toLong()
                        categoryTypeMappingId=selectedSubCategory.categoryType?.id
                        area=etArea.text.toString().toLong()
                        longitude=Preferences.fetchLocation()?.lng
                        latitude=Preferences.fetchLocation()?.lat
                        if(selectedSubCategory.categoryType?.getCategoryTypeEnum()==CategoryTypeEnum.Delivery){
                            driverLicenseNo=etDrivingLicense.text.toString()
                           if(isIdUploaded){
                               aadhaarFileName=idsDetail?.aadhaar
                               photoFileName=idsDetail?.photo
                               licenseFileName=idsDetail?.licence
                           } else{
                               Toast.makeText(this@HighFlyerRegistration, getString(R.string.upload_id_card_images_first),Toast.LENGTH_SHORT).show()
                               return@setOnClickListener
                           }
                        }
                    }
                    Log.d("TAG", Gson().toJson(highFlyerData))
                    mBinding.progressBar.visibility=View.VISIBLE
                    mBinding.buttonNext.visibility=View.GONE
                    highFlyerRegistrationViewModel.registerHighFlyer(highFlyerData)
                    Log.d(this@HighFlyerRegistration.localClassName,highFlyerData.toString())
                }
            }
            val catList=Preferences.fetchCategories()?.data?.categoryList
            var subCatList:MutableList<SubCategory>
            spinnerBusinessCategory.apply {
                adapter=ArrayAdapter(this@HighFlyerRegistration,R.layout.support_simple_spinner_dropdown_item,catList!!)
                onItemSelectedListener=object:OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        selectedCategoryId= catList[p2].id!!
                        spinnerBusinessSubCategory.let {
                            visibility=View.VISIBLE
                            subCatList=catList[p2].subCategoryList!!
                            it.adapter=ArrayAdapter(this@HighFlyerRegistration,R.layout.support_simple_spinner_dropdown_item,subCatList)
                            it.onItemSelectedListener=object :OnItemSelectedListener{
                                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long
                                ) {
                                    selectedSubCategory=subCatList[p2].apply {
                                        selectedSubCategoryId=id!!
                                        if(this.categoryType?.getCategoryTypeEnum()==CategoryTypeEnum.Delivery){
                                            idUploadLayout.visibility=View.VISIBLE
                                        } else{
                                            idUploadLayout.visibility=View.GONE
                                        }

                                    }
                                }
                                override fun onNothingSelected(p0: AdapterView<*>?) { Toast.makeText(this@HighFlyerRegistration,"choose one sub category first!",Toast.LENGTH_SHORT).show() }
                            }
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) { Toast.makeText(this@HighFlyerRegistration,"choose one category first!",Toast.LENGTH_SHORT).show() }
                }
            }

            adharPickBtn.setOnClickListener {contract.launch("image/*");imageUploadTag= Constants.AADHAAR}
            photoPickBtn.setOnClickListener {contract.launch("image/*");imageUploadTag= Constants.PHOTO}
            licensePickBtn.setOnClickListener {contract.launch("image/*");imageUploadTag= Constants.LICENCE}


        }


    }


    private fun onIdUploaded(state: State<IdsUploadResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                mBinding.progressBar.visibility=View.GONE
                mBinding.buttonNext.visibility=View.VISIBLE
                Utility.performErrorState(this,state, getString(R.string.image_upload_failed)+"\n"+state.exception.errorMessage)
            }

            is State.DataState -> {
                Log.d("TAG",state.data.data.toString())
                mBinding.progressBar.visibility=View.GONE
                mBinding.buttonNext.visibility=View.VISIBLE
                idsDetail=state.data.data!!
                isIdUploaded=true
                Toast.makeText(this@HighFlyerRegistration,"uploading images success!",Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun onRegistrationPerform(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.d(this.localClassName,"state.loading"+state.isLoading.toString())
            }

            is State.ErrorState -> {
                mBinding.progressBar.visibility=View.GONE
                mBinding.buttonNext.visibility=View.VISIBLE
                Utility.performErrorState(this,state, getString(R.string.signup_failed)+"\n"+state.exception.errorMessage)
            }

            is State.DataState -> {
                mBinding.progressBar.visibility=View.GONE
                mBinding.buttonNext.visibility=View.VISIBLE
                Toast.makeText(applicationContext,
                    getString(R.string.success),Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext,HighFlyerTermsActivity::class.java))
                userViewModel.getUser(Preferences.fetchToken())
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
                finishAffinity()
            }

            is State.DataState -> {
                Preferences.storeUser(state.data.data!!)
                Log.d(this.localClassName,"success userSaving")
                finishAffinity()
            }
            else -> {}
        }
    }
    private fun isValid(): Boolean {
        mBinding.apply {
            cleanError(listOf(tlMobileNumber,tlArea,tlBusinessName,tlBusinessNumber))
        }
        var result = true
        when {

            mBinding.etMobileNumber.text?.trim().isNullOrEmpty() -> {
                result = false
                mBinding.tlMobileNumber.error = getString(R.string.lbl_required)
                highlightErrorField(mBinding.parentScrollView,mBinding.tlMobileNumber)
            }

            mBinding.etMobileNumber.text?.trim()?.length != 10 -> {
                result = false
                mBinding.tlMobileNumber.error = getString(R.string.lbl_invalid)
                highlightErrorField(mBinding.parentScrollView,mBinding.tlMobileNumber)
            }

            mBinding.etBusinessNumber.text?.trim()?.length != 10 -> {
                result = false
                mBinding.tlBusinessNumber.error = getString(R.string.lbl_invalid)
                highlightErrorField(mBinding.parentScrollView,mBinding.tlBusinessNumber)
            }

            mBinding.etBusinessName.text?.trim().isNullOrEmpty() -> {
                result = false
                mBinding.tlBusinessName.error = getString(R.string.lbl_required)
                highlightErrorField(mBinding.parentScrollView,mBinding.tlBusinessName)
            }

            mBinding.etArea.text?.trim().isNullOrEmpty() -> {
                result = false
                mBinding.tlArea.error = getString(R.string.lbl_required)
                highlightErrorField(mBinding.parentScrollView,mBinding.tlArea)
            }
        }
        return result
    }

}