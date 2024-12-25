package com.fststep.nxt2me.highflyer.dashboard

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
import com.fststep.nxt2me.databinding.ActivityAddPropertyItemBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPropertyItem : AppCompatActivity(),ProductButtonListener {
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var mBinding: ActivityAddPropertyItemBinding
    lateinit var contract: ActivityResultLauncher<String>
    var oldProduct: Product?=null
    var image: Uri?=null
    var spinnerSelectedItem="Rent";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddPropertyItemBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.propertyOnSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                spinnerSelectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
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
                    Toast.makeText(this@AddPropertyItem, getString(R.string.add_previous_item_first),
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
            etPropertyName.setText(oldProduct?.productName)
            etTotalCost.setText(oldProduct?.productDetails?.totalCost)
            etDesc.setText(oldProduct?.productDetails?.desc)
            layourSelectedImage.visibility= View.VISIBLE
            Glide.with(this@AddPropertyItem).load(getImageDownloadUrl(oldProduct?.productImageUrl!!)).into(ivSelectedImage)
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
                Toast.makeText(this@AddPropertyItem,"success!", Toast.LENGTH_SHORT).show()
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
            Utility.cleanError(arrayListOf(tlTotalCost,tlTotalCost,tlPropertyName))
            if(etTotalCost.text.isNullOrEmpty()){
                etTotalCost.error="Required!"
                return false
            }else if(etPropertyName.text.isNullOrEmpty()){
                tlPropertyName.error="Required!"
                return false
            }else if(etDesc.text.isNullOrEmpty()){
                tlDesc.error="Required!"
                return false
            } else if(spinnerSelectedItem.isEmpty()){
                Toast.makeText(this@AddPropertyItem,"select property type first!",Toast.LENGTH_SHORT).show()
                return false
            }
            else if(etTotalCost.text.isNullOrEmpty()){
                tlTotalCost.error="Required!"
                return false
            }else if(image==null && oldProduct==null ){
                Toast.makeText(this@AddPropertyItem, getString(R.string.upload_image_first), Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
    fun clearForm(){
        mBinding.apply {
            cardProductForm.visibility= View.GONE
            etPropertyName.setText("")
            etTotalCost.setText("")
            etDesc.setText("")
            spinnerSelectedItem=""
            layourSelectedImage.visibility= View.GONE
        }
    }
    fun uploadData(){
        val newProduct= ProductRequest().apply {
            mBinding.apply {
                name=etPropertyName.text.toString()
                productDetails= ProductDetail().apply {
                    totalCost=etTotalCost.text.toString()
                    desc=etDesc.text.toString()
                    rentOrSale=spinnerSelectedItem
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
                    multiPartBodyFromUri(this@AddPropertyItem,image!!,"fileToUpload")
                )
            else
                productViewModel.updateProduct(oldProduct!!.productId!!,
                    partFromString(Gson().toJson(newProduct))
                )
        }else{
            productViewModel.addProduct(
                partFromString(Gson().toJson(newProduct)),
                multiPartBodyFromUri(this@AddPropertyItem,image!!,"fileToUpload")
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
                layoutManager= LinearLayoutManager(this@AddPropertyItem)
                adapter=ProductAdapter(this@AddPropertyItem,this@AddPropertyItem,data)
            }
        }
    }

    override fun edit(position: Int, product: Product) {
        val intent= Intent(this@AddPropertyItem,AddGoodItem::class.java).putExtra(
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