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
import com.fststep.nxt2me.databinding.ActivityAddPhoneItemBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPhoneItem : AppCompatActivity(),ProductButtonListener {
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var mBinding: ActivityAddPhoneItemBinding
    lateinit var contract: ActivityResultLauncher<String?>
    var oldProduct: Product?=null
    var image: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddPhoneItemBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        oldProduct= Gson().fromJson(intent.getStringExtra(Constants.KEY_PRODUCT)?:"", Product::class.java)
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
        mBinding.apply {
            ivClearImage.setOnClickListener{
                image=null;
                layourSelectedImage.visibility= View.GONE
            }
            btAddMore.setOnClickListener{
                if(cardProductForm.visibility== View.VISIBLE)
                    Toast.makeText(this@AddPhoneItem, getString(R.string.add_previous_item_first),
                        Toast.LENGTH_SHORT).show()
                cardProductForm.visibility= View.VISIBLE
            }
            buttonSave.setOnClickListener { onBackPressed();finish() }
            btAddMenu.setOnClickListener {
                if(isValid()){
                    uploadData()
                }
            }
            ivCamera.setOnClickListener { contract.launch("image/*") }
            toolbar.btBack.setOnClickListener { onBackPressed() }

        }
        refreshProductList()

    }


    private fun loadDataForEditItem() {
        mBinding.apply {
            cardProductForm.visibility= View.VISIBLE
            etProductName.setText(oldProduct?.productName)
            etDiscount.setText(oldProduct?.productDetails?.discount)
            etMarketPrice.setText(oldProduct?.productDetails?.price)
            etProductDescription.setText(oldProduct?.productDetails?.desc)
            layourSelectedImage.visibility= View.VISIBLE
            Glide.with(this@AddPhoneItem).load(getImageDownloadUrl(oldProduct?.productImageUrl!!)).into(ivSelectedImage)
        }
    }

    private fun onProductDelete(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i(this.localClassName,"is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                Log.d(this.localClassName,"state.error product delete Failed"+state.exception.errorMessage + state.exception.errorCode)
                mBinding.progressBar.visibility= View.GONE
            }

            is State.DataState -> {
                refreshProductList()
                mBinding.progressBar.visibility= View.GONE
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
                initProductList(state.data.data?.content!!)
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
                refreshProductList()
                Toast.makeText(this@AddPhoneItem,"success!", Toast.LENGTH_SHORT).show()
                Log.d(this.localClassName,"success product upload")
                clearForm()
                mBinding.progressBar.visibility= View.GONE
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
            Utility.cleanError(arrayListOf(tlDiscount,tlMarketPrice,tlProductName,tlProductDescription))
            if(etDiscount.text.isNullOrEmpty()){
                tlDiscount.error="Required!"
                return false
            }else if(etMarketPrice.text.isNullOrEmpty()){
                tlMarketPrice.error="Required!"
                return false
            }else if(etProductName.text.isNullOrEmpty()){
                tlProductName.error="Required!"
                return false
            }else if(etProductDescription.text.isNullOrEmpty()){
                tlProductDescription.error="Required!"
                return false
            }else if(image==null && oldProduct==null ){
                Toast.makeText(this@AddPhoneItem, getString(R.string.upload_image_first), Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
    fun clearForm(){
        mBinding.apply {
            cardProductForm.visibility= View.GONE
            etProductName.setText("")
            etDiscount.setText("")
            etMarketPrice.setText("")
            etProductDescription.setText("")
            layourSelectedImage.visibility= View.GONE
        }
    }
    fun uploadData(){
        val newProduct= ProductRequest().apply {
            mBinding.apply {
                name=etProductName.text.toString()
                productDetails= ProductDetail().apply {
                    price=etMarketPrice.text.toString()
                    discount=etDiscount.text.toString()
                    desc=etProductDescription.text.toString()
                }
            }
            isHighFlyer=true
            productTypeId= Preferences.fetchMySubCat()?.categoryType?.id
            userId= Preferences.fetchUser()?.highFlyerDto?.id
        }
        Log.d("TAG", Gson().toJson(newProduct))
        mBinding.progressBar.visibility= View.VISIBLE
        if(oldProduct!=null){
            if(image!=null)
                productViewModel.updateProduct(oldProduct!!.productId!!,
                    partFromString(Gson().toJson(newProduct)),
                    multiPartBodyFromUri(this@AddPhoneItem,image!!,"fileToUpload")
                )
            else
                productViewModel.updateProduct(oldProduct!!.productId!!,
                    partFromString(Gson().toJson(newProduct))
                )
        }else{
            productViewModel.addProduct(
                partFromString(Gson().toJson(newProduct)),
                multiPartBodyFromUri(this@AddPhoneItem,image!!,"fileToUpload")
            )
        }


    }
    private fun showSelectedImage(uri: Uri) {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        mBinding.apply {
            layourSelectedImage.visibility= View.VISIBLE
            ivSelectedImage.setImageBitmap(bitmap)
        }
    }
    fun initProductList(data:List<Product>){
        mBinding.apply {
            rvMenu.apply {
                visibility= View.VISIBLE
                layoutManager= LinearLayoutManager(this@AddPhoneItem)
                adapter=ProductAdapter(this@AddPhoneItem,this@AddPhoneItem,data)
            }
        }
    }

    override fun edit(position: Int, product: Product) {
        val intent= Intent(this@AddPhoneItem,AddPhoneItem::class.java).putExtra(
            Constants.KEY_PRODUCT,
            Gson().toJson(product))
        startActivity(intent)
        finish()
    }

    override fun delete(position: Int, product: Product) {
        productViewModel.deleteProduct(product.productId!!)
        mBinding.progressBar.visibility= View.VISIBLE
    }
}