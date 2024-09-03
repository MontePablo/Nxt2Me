package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.Seller
import com.fststep.nxt2me.core.data.models.SellerListResponse
import com.fststep.nxt2me.core.data.models.SellerSearchRequest
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.databinding.ActivitySellerListBinding
import com.fststep.nxt2me.dashboard.SellerViewModel
import com.fststep.nxt2me.dashboard.view.adapter.SellerAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerListActivity : AppCompatActivity(),SellerListener {
    lateinit var mBinding:ActivitySellerListBinding
    private val sellerViewModel: SellerViewModel by viewModels()
    var catId:String?=null
    var subCatId:String?=null
    var subCatName:String?=null
    lateinit var mSellerAdapter:SellerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivitySellerListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        catId=intent.getStringExtra(Constants.KEY_CATEGORY_ID)
        subCatId=intent.getStringExtra(Constants.KEY_SUB_CATEGORY_ID)
        subCatName=intent.getStringExtra(Constants.KEY_SUB_CATEGORY)
        sellerViewModel.apply {
            observe(sellerListResponse,::onSellerListReceive)
        }

        initRecyclerview()
        fetchList()
        mBinding.apply {
            ivBackArrow.setOnClickListener { onBackPressed() }
            if(subCatName!=null)
                tvTitle.setText(subCatName)
        }
    }

    private fun initRecyclerview() {
        mBinding.rvMerchantList.apply {
            mSellerAdapter= SellerAdapter(this@SellerListActivity)
            adapter=mSellerAdapter
            layoutManager=GridLayoutManager(this@SellerListActivity,3)
        }
    }

    fun fetchList(){
        if(catId!=null && subCatId!=null){
            val data=SellerSearchRequest().apply {
                categoryId=catId?.toLong()
                subCategoryId=subCatId?.toLong()
                Preferences.fetchLocation()?.apply { longitude=lng;latitude=lat }
                limitedDistance=1000
                page=0
                size=1000
            }
            sellerViewModel.searchSeller(data)
            mBinding.progressBar.visibility=View.VISIBLE
        }
    }
    private fun onSellerListReceive(state: State<SellerListResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i("TAG","is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(this@SellerListActivity,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                mBinding.progressBar.visibility= View.GONE
            }

            is State.DataState -> {
                mBinding.progressBar.visibility= View.GONE
                if(state.data.data?.content!!.isNotEmpty())
                    mSellerAdapter.updateData(state.data.data?.content!!)
                else
                    mBinding.noItemMessage.visibility=View.VISIBLE
            }
            else -> {}
        }
    }

    override fun onSellerClick(seller: Seller) {
        startActivity(Intent(this,ProductListActivity::class.java).putExtra(Constants.SELLER,Gson().toJson(seller)))
    }
}
interface SellerListener{
    fun onSellerClick(seller: Seller)
}