package com.fststep.nxt2me.dashboard.view


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.dashboard.view.adapter.ProductAdapter
import com.fststep.nxt2me.databinding.ActivityProductListBinding
import com.fststep.nxt2me.highflyer.dashboard.ProductViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity(), ProductListener {
    private val productViewModel: ProductViewModel by viewModels()
    lateinit var mAdapter: ProductAdapter
    lateinit var mBinding: ActivityProductListBinding
    lateinit var type:CategoryTypeEnum
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityProductListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val seller= Gson().fromJson(intent.getStringExtra(Constants.SELLER),Seller::class.java)
        type=getType(seller)!!
        initRecyclerView()
        refreshProductList(seller)
        productViewModel.apply {
            observe(productListResponse,::onProductListDownload)
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
                updateProductList(state.data.data?.content?: listOf())
            }
            else -> {}
        }
    }
    private fun refreshProductList(seller:Seller) {
        val searchReq= ProductSearchRequest().apply {
            size = 1000
            userId=seller.id
            categoryId=seller.categoryId?.toInt()
            subCategoryId=seller.subCategoryId?.toInt()
            limitedDistance=10000
            Preferences.fetchLocation().apply {
                longitude=this?.lng;latitude=this?.lat
            }
            page=0

        }
        productViewModel.searchProduct(searchReq)
    }
    fun initRecyclerView(){
        mBinding.apply {
            rvItemList.apply {
                visibility= View.VISIBLE
                layoutManager= LinearLayoutManager(this@ProductListActivity)
                mAdapter= ProductAdapter(this@ProductListActivity,type,this@ProductListActivity)
                adapter=mAdapter
            }
        }
    }
    fun updateProductList(data:List<Product>){
        mAdapter.updateData(data)
    }

    override fun onClickProduct(product: Product) {
        TODO("Not yet implemented")
    }
    fun getType(seller:Seller):CategoryTypeEnum?{
        for(cat in Preferences.fetchCategories()?.data?.categoryList!!){
            if(seller.categoryId==cat.id){
                for(subcat in cat.subCategoryList!!){
                    if(seller.subCategoryId==subcat.id){
                        if(subcat.subCategoryName?.contains("broker",true) == true){
                            return CategoryTypeEnum.Property
                        }
                        return subcat.categoryType?.getCategoryTypeEnum()
                    }
                }
            }
        }
        return null
    }


}
interface ProductListener{
    fun onClickProduct(product:Product)
}