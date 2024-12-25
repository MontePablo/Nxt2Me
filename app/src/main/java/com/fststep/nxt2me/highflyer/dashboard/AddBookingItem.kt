package com.fststep.nxt2me.highflyer.dashboard

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.ProductDetail
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductRequest
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.extension.getImageDownloadUrl
import com.fststep.nxt2me.core.extension.multiPartBodyFromUri
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.extension.partFromString
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.dashboard.view.DashboardActivity
import com.fststep.nxt2me.databinding.ActivityAddBookingItemBinding
import com.fststep.nxt2me.highflyer.registration.HighFlyerTermsActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookingItem : AppCompatActivity(),ProductButtonListener {
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var mBinding:ActivityAddBookingItemBinding
    lateinit var contract: ActivityResultLauncher<String>
    lateinit var mAdapter: ProductAdapter
    var oldProduct:Product?=null
    var image: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddBookingItemBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initRecyclerView()
        oldProduct=Gson().fromJson(intent.getStringExtra(Constants.KEY_PRODUCT)?:"",Product::class.java)
        if(oldProduct!=null)
            loadDataForEditItem()
        contract=registerForActivityResult(ActivityResultContracts.GetContent()){
            if(it!=null) {
                image=it
                showSelectedImage(it)
            }
        }
        productViewModel.apply {
            observe(productUploadResponse,::onProductUpload)
            observe(productListResponse,::onProductListDownload)
            observe(productDeleteResponse,::onProductDelete)
        }
        refreshProductList()
        mBinding.apply {
            ivClearImage.setOnClickListener{
                image=null;
                layourSelectedImage.visibility=View.GONE
            }
            btAddMore.setOnClickListener{
                if(cardProductForm.visibility==View.VISIBLE)
                    Toast.makeText(this@AddBookingItem, getString(R.string.add_previous_item_first),Toast.LENGTH_SHORT).show()
                cardProductForm.visibility=View.VISIBLE
            }
            btAddMenu.setOnClickListener {
                if(isValid()){
                    uploadData()
                }
            }
            buttonSave.setOnClickListener { onBackPressed();finish() }
            ivCamera.setOnClickListener { contract.launch("image/*") }
            toolbar.btBack.setOnClickListener { onBackPressed() }
        }

    }

    override fun onBackPressed() {
        if(Preferences.fetchUser()?.highFlyerDto?.isTCAccepted==false)
            startActivity(Intent(this@AddBookingItem,HighFlyerTermsActivity::class.java))
        else
            super.onBackPressed()
    }   

    private fun loadDataForEditItem() {
        mBinding.apply {
            cardProductForm.visibility=View.VISIBLE
            etServiceName.setText(oldProduct?.productName)
            etDiscount.setText(oldProduct?.productDetails?.discount)
            etMarketPrice.setText(oldProduct?.productDetails?.price)
            etTotalCost.setText(oldProduct?.productDetails?.totalCost)
            layourSelectedImage.visibility=View.VISIBLE
            Glide.with(this@AddBookingItem).load(getImageDownloadUrl(oldProduct?.productImageUrl!!)).into(ivSelectedImage)
        }
    }

    private fun onProductDelete(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName,"is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Toast.makeText(this@AddBookingItem,"deleted",Toast.LENGTH_SHORT).show()
                Utility.performErrorState(this,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                Log.d(this.localClassName,"state.error product delete Failed"+state.exception.errorMessage + state.exception.errorCode)
                mBinding.progressBar.visibility=View.GONE
            }

            is State.DataState -> {
                refreshProductList()
                mBinding.progressBar.visibility=View.GONE
            }
            else -> {}
        }
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
                updateProductList(state.data.data?.content!!)
                mBinding.apply {
                    nestedScrollView.post {
                       nestedScrollView.scrollTo(0, cardProductForm.top)
                    }
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
                mBinding.progressBar.visibility=View.GONE
            }

            is State.DataState -> {
                refreshProductList()
                Toast.makeText(this@AddBookingItem,"success!",Toast.LENGTH_SHORT).show()
                Log.d(this.localClassName,"success product upload")
                clearForm()
                mBinding.progressBar.visibility=View.GONE
            }
            else -> {}
        }
    }

    private fun refreshProductList() {
        val searchReq=ProductSearchRequest().apply {
            size = 1000
            Preferences.fetchUser().apply {
                userId=this?.highFlyerDto?.id!!
            }
            categoryId=Preferences.fetchMyCat()?.id?.toInt()
            subCategoryId=Preferences.fetchMySubCat()?.id?.toInt()
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
            Utility.cleanError(arrayListOf(tlDiscount,tlMarketPrice,tlTotalCost,tlServiceName))
            if(etDiscount.text.isNullOrEmpty()){
                tlDiscount.error="Required!"
                return false
            }else if(etMarketPrice.text.isNullOrEmpty()){
                tlMarketPrice.error="Required!"
                return false
            }else if(etServiceName.text.isNullOrEmpty()){
                tlServiceName.error="Required!"
                return false
            }else if(etTotalCost.text.isNullOrEmpty()){
                tlTotalCost.error="Required!"
                return false
            }
        }
        return true
    }
    fun clearForm(){
        mBinding.apply {
            cardProductForm.visibility=View.GONE
            etServiceName.setText("")
            etDiscount.setText("")
            etMarketPrice.setText("")
            etTotalCost.setText("")
            layourSelectedImage.visibility=View.GONE
        }
    }
    fun uploadData(){
        val newProduct=ProductRequest().apply {
            mBinding.apply {
                name=etServiceName.text.toString()
                productDetails= ProductDetail().apply {
                    price=etMarketPrice.text.toString()
                    discount=etDiscount.text.toString()
                    totalCost=etTotalCost.text.toString()
                }
            }
            isHighFlyer=true
            productTypeId=Preferences.fetchMySubCat()?.categoryType?.id
            userId=Preferences.fetchUser()?.highFlyerDto?.id
        }
        Log.d("TAG", Gson().toJson(newProduct))
        Log.d("TAG", "image:"+image.toString()?:"")

        mBinding.progressBar.visibility=View.VISIBLE
        if(oldProduct!=null){
            if(image!=null)
                productViewModel.updateProduct(oldProduct!!.productId!!,
                    partFromString(Gson().toJson(newProduct)),
                    multiPartBodyFromUri(this@AddBookingItem,image!!,"fileToUpload")
                )
            else
                productViewModel.updateProduct(oldProduct!!.productId!!,
                    partFromString(Gson().toJson(newProduct))
                )
        }else{
            productViewModel.addProduct(
                partFromString(Gson().toJson(newProduct)),
                multiPartBodyFromUri(this@AddBookingItem,image!!,"fileToUpload")
            )
        }
    }
    private fun showSelectedImage(uri: Uri) {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        mBinding.apply {
            layourSelectedImage.visibility=View.VISIBLE
            ivSelectedImage.setImageBitmap(bitmap)
        }
    }
    fun updateProductList(data:List<Product>){
        mAdapter.updateData(data)
    }
    fun initRecyclerView(){
        mBinding.apply {
            rvMenu.apply {
                visibility=View.VISIBLE
                layoutManager=LinearLayoutManager(this@AddBookingItem)
                mAdapter=ProductAdapter(this@AddBookingItem,this@AddBookingItem)
                adapter=mAdapter
            }
        }
    }

    override fun edit(position: Int, product: Product) {
        oldProduct=product
        loadDataForEditItem()
    }

    override fun delete(position: Int, product: Product) {
        productViewModel.deleteProduct(product.productId!!)
        mBinding.progressBar.visibility=View.VISIBLE
    }
}