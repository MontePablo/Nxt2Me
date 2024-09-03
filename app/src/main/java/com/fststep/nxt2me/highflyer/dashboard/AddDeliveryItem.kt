package com.fststep.nxt2me.highflyer.dashboard

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.ProductDetail
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductRequest
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.extension.multiPartBodyFromUri
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.extension.partFromString
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.databinding.ActivityAddDeliveryItemBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDeliveryItem : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var mBinding: ActivityAddDeliveryItemBinding
    lateinit var contract: ActivityResultLauncher<String?>
    var oldProductId1=""
    var oldProductId2=""
    var is1stProductUploadFinished=false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddDeliveryItemBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        productViewModel.apply {
            observe(productUploadResponse,::onProductUpload)
            observe(productListResponse,::onProductListDownload)
        }
        mBinding.apply {
            buttonSave.setOnClickListener {
                if(isValid()){
                    uploadData()
                }
            }

            toolbar.btBack.setOnClickListener { onBackPressed() }

        }
        refreshProductList()

    }




    private fun onProductListDownload(state: State<ProductListResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName,"is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                Log.d(this.localClassName,"state.error productlist fetch Failed"+state.exception.errorMessage + state.exception.errorCode)
            }

            is State.DataState -> {
                if(state.data.data?.content!=null && state.data.data?.content!!.size==2){
                    initProductList(state.data.data?.content!!)
                }
            }
            else -> {}
        }
    }

    private fun onProductUpload(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName,"is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                Log.d(this.localClassName,"state.error upload product Failed"+state.exception.errorMessage + state.exception.errorCode)
                mBinding.progressBar.visibility= View.GONE
            }

            is State.DataState -> {
                Toast.makeText(this,"success!", Toast.LENGTH_SHORT).show()
                Log.d(this.localClassName,"success product upload")
                mBinding.progressBar.visibility= View.GONE

                if(is1stProductUploadFinished){
                    onBackPressed();finish()
                }else
                    is1stProductUploadFinished=true
            }
            else -> {}
        }
    }

    private fun refreshProductList() {
        val searchReq= ProductSearchRequest().apply {
            size = 1000
            Preferences.fetchUser().apply {
                userId=this?.highFlyerDto?.id!!
            }
            categoryId= Preferences.fetchMyCat()?.id?.toInt()
            subCategoryId= Preferences.fetchMySubCat()?.id?.toInt()
            limitedDistance=10000
            Preferences.fetchLocation().apply {
                longitude=this?.lng;latitude=this?.lat
            }
            page=0

        }
        productViewModel.searchProduct(searchReq)
    }

    fun isValid():Boolean{
        mBinding.apply {
            Utility.cleanError(arrayListOf(tlInternetCharge1,tlInternetCharge2,tlPrice1,tlPrice2,tlTotalCost1,tlTotalCost2))
            if(etInternetCharge1.text.isNullOrEmpty()){
                tlInternetCharge1.error="Required!"
                return false
            }else if(etPrice1.text.isNullOrEmpty()){
                tlPrice1.error="Required!"
                return false
            }else if(etTotalCost1.text.isNullOrEmpty()){
                tlTotalCost1.error="Required!"
                return false
            }else if(etInternetCharge2.text.isNullOrEmpty()){
                tlInternetCharge2.error="Required!"
                return false
            }else if(etPrice2.text.isNullOrEmpty()){
                tlPrice2.error="Required!"
                return false
            }else if(etTotalCost2.text.isNullOrEmpty()){
                tlTotalCost2.error="Required!"
                return false
            }
        }
        return true
    }

    fun uploadData(){
        val newProduct1= ProductRequest().apply {
            mBinding.apply {
                name=name1.text.toString()
                productDetails= ProductDetail().apply {
                    price=etPrice1.text.toString()
                    totalCost=etTotalCost1.text.toString()
                    internetCharge=etInternetCharge1.text.toString()
                }
            }
            isHighFlyer=true
            productTypeId= Preferences.fetchMySubCat()?.categoryType?.id
            userId= Preferences.fetchUser()?.highFlyerDto?.id
        }
        Log.d("TAG", Gson().toJson(newProduct1))

        val newProduct2= ProductRequest().apply {
            mBinding.apply {
                name=name2.text.toString()
                productDetails= ProductDetail().apply {
                    price=etPrice2.text.toString()
                    totalCost=etTotalCost2.text.toString()
                    internetCharge=etInternetCharge2.text.toString()
                }
            }
            isHighFlyer=true
            productTypeId= Preferences.fetchMySubCat()?.categoryType?.id
            userId= Preferences.fetchUser()?.highFlyerDto?.id
        }
        Log.d("TAG", Gson().toJson(newProduct2))
        mBinding.progressBar.visibility= View.VISIBLE

        if(oldProductId1.isNotEmpty() && oldProductId2.isNotEmpty())       {
            productViewModel.updateProduct(oldProductId1, partFromString(Gson().toJson(newProduct1)))
            productViewModel.updateProduct(oldProductId2, partFromString(Gson().toJson(newProduct2)))

        } else{
            productViewModel.addProduct(partFromString(Gson().toJson(newProduct1)))
            productViewModel.addProduct(partFromString(Gson().toJson(newProduct2)))
        }

    }
    fun initProductList(data:List<Product>){
        mBinding.apply {
            etPrice1.setText(data[0].productDetails?.price)
            etInternetCharge1.setText(data[0].productDetails?.internetCharge)
            etTotalCost1.setText(data[0].productDetails?.totalCost)
            oldProductId1=data[0].productId!!

            etPrice2.setText(data[1].productDetails?.price)
            etInternetCharge2.setText(data[1].productDetails?.internetCharge)
            etTotalCost2.setText(data[1].productDetails?.totalCost)
            oldProductId2=data[1].productId!!

        }
    }
}