/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.core.data.models.CategoryTypeEnum
import com.fststep.nxt2me.core.data.models.CommonResponse
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.core.data.models.ProductListResponse
import com.fststep.nxt2me.core.data.models.ProductSearchRequest
import com.fststep.nxt2me.core.extension.observe
import com.fststep.nxt2me.core.retrofit.State
import com.fststep.nxt2me.core.util.Utility
import com.fststep.nxt2me.core.view.GenerateCouponsActivity
import com.fststep.nxt2me.core.view.ImagePickerDialog
import com.fststep.nxt2me.databinding.FragmentMenuBinding
import com.fststep.nxt2me.highflyer.registration.ServiceType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MenuFragment : Fragment(),ProductButtonListener {
    private val productViewModel: ProductViewModel by viewModels()

    private lateinit var mBinding: FragmentMenuBinding

    private var products: ArrayList<Producttt> = arrayListOf()

    private lateinit var mAdapter: ProductAdapter

    private var currentPhotoPath: String = ""

    private var editModeImageSelectionCallback: ((path: String) -> Unit)? = null

    private var serviceType: ServiceType? = null

    companion object {
        fun newInstance(serviceType: ServiceType): MenuFragment {
            val frag = MenuFragment()
            frag.serviceType = serviceType
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMenuBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.apply {
            observe(productListResponse,::onProductListDownload)
            observe(productDeleteResponse,::onProductDelete)
        }
        mBinding.btAddServiceBooking.setOnClickListener {
                startActivity(Intent(requireContext(),AddBookingItem::class.java))
        }
        mBinding.btAddServicePhone.setOnClickListener {
            startActivity(Intent(requireContext(),AddPhoneItem::class.java))
        }
        mBinding.btAddItemGoods.setOnClickListener {
            if(Preferences.fetchMySubCat()?.subCategoryName?.lowercase()?.contains("broker") == true)
                startActivity(Intent(requireContext(),AddPropertyItem::class.java))
            else
                startActivity(Intent(requireContext(),AddGoodItem::class.java))
        }
        mBinding.btAddServiceDelivery.setOnClickListener {
            startActivity(Intent(requireContext(),AddDeliveryItem::class.java))
        }
        when(Preferences.fetchMySubCat()?.categoryType?.getCategoryTypeEnum()){
            CategoryTypeEnum.PhoneNumber -> mBinding.barLayoutPhone.visibility=View.VISIBLE
            CategoryTypeEnum.Booking -> mBinding.barLayoutBooking.visibility=View.VISIBLE
            CategoryTypeEnum.Good -> mBinding.barLayoutGoods.visibility=View.VISIBLE
            CategoryTypeEnum.Delivery -> mBinding.barLayoutDelivery.visibility=View.VISIBLE

            else -> {}
        }
        initRecyclerView()
        refreshProductList()
        mBinding.btGenerateCouponBooking.setOnClickListener { goToGenerateCoupon() }
        mBinding.btGenerateCouponGoods.setOnClickListener { goToGenerateCoupon() }
        mBinding.btGenerateCouponPhone.setOnClickListener { goToGenerateCoupon() }
    }

    private fun goToGenerateCoupon() {
        startActivity(Intent(requireActivity(), GenerateCouponsActivity::class.java))
    }

    fun updateProductList(data:List<Product>){
        if(data.size>0){
            mAdapter.updateData(data)
            mBinding.tvEmptyMessage.visibility=View.GONE
        }else
            mBinding.tvEmptyMessage.visibility=View.VISIBLE

    }
    fun initRecyclerView(){
        mBinding.rvSavedProducts.apply {
            layoutManager=LinearLayoutManager(requireContext())
            mAdapter=ProductAdapter(requireContext(),this@MenuFragment)
            adapter=mAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        refreshProductList()
    }
    override fun edit(position: Int, product: Product) {
        when(Preferences.fetchMySubCat()?.categoryType?.getCategoryTypeEnum()){
            CategoryTypeEnum.PhoneNumber ->  startActivity(Intent(requireContext(),AddPhoneItem::class.java).putExtra(Constants.KEY_PRODUCT, Gson().toJson(product)))
            CategoryTypeEnum.Booking ->  startActivity(Intent(requireContext(),AddBookingItem::class.java).putExtra(Constants.KEY_PRODUCT, Gson().toJson(product)))
            CategoryTypeEnum.Good -> {
                if(Preferences.fetchMySubCat()?.subCategoryName?.lowercase()?.contains("broker") == true)
                    startActivity(Intent(requireContext(),AddPropertyItem::class.java).putExtra(Constants.KEY_PRODUCT, Gson().toJson(product)))
                else
                    startActivity(Intent(requireContext(),AddGoodItem::class.java).putExtra(Constants.KEY_PRODUCT, Gson().toJson(product)))
            }
            else -> {}
        }
    }

    override fun delete(position: Int, product: Product) {
        productViewModel.deleteProduct(product.productId!!)
        mBinding.progressBar.visibility=View.VISIBLE
    }
    private fun refreshProductList() {
//        val searchReq= ProductSearchRequest().apply {
//            size = 1000
//            Preferences.fetchUser().apply {
//                userId=this?.highFlyerDto?.id!!
//            }
//            categoryId= Preferences.fetchMyCat()?.id?.toInt()
//            subCategoryId= Preferences.fetchMySubCat()?.id?.toInt()
//            limitedDistance=10000
//            Preferences.fetchLocation().apply {
//                longitude=this?.lng;latitude=this?.lat
//            }
//            page=0
//
//        }
//        productViewModel.searchProduct(searchReq)
    }
    private fun onProductListDownload(state: State<ProductListResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i("TAG","is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(requireActivity() as AppCompatActivity,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                Log.d("TAG","state.error productlist fetch Failed"+state.exception.errorMessage + state.exception.errorCode)
            }

            is State.DataState -> {
                updateProductList(state.data.data?.content!!)
            }
            else -> {}
        }
    }
    private fun onProductDelete(state: State<CommonResponse>?) {
        when (state) {
            is State.LoadingState -> {
                Log.i("TAG","is loading"+ state.isLoading.toString())
            }

            is State.ErrorState -> {
                Utility.performErrorState(requireActivity() as AppCompatActivity,state, getString(R.string.failed) +"\n"+ state.exception.errorMessage)
                mBinding.progressBar.visibility=View.GONE
            }

            is State.DataState -> {
                refreshProductList()
                Toast.makeText(context,"deleted",Toast.LENGTH_SHORT).show()
                mBinding.progressBar.visibility=View.GONE
            }
            else -> {}
        }
    }


}
