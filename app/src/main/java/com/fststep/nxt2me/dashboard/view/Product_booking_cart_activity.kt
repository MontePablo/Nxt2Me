package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Constants
import com.fststep.nxt2me.core.data.models.CreateOrderRequest
import com.fststep.nxt2me.core.data.models.Product
import com.fststep.nxt2me.dashboard.view.adapter.ProductBookingCartAdapter
import com.fststep.nxt2me.dashboard.view.adapter.ProductGoodsCartAdapter
import com.fststep.nxt2me.databinding.ActivityProductBookingCartBinding
import com.google.gson.Gson

class Product_booking_cart_activity : AppCompatActivity() {
    lateinit var mBinding: ActivityProductBookingCartBinding
    lateinit var createOrderRequest: CreateOrderRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityProductBookingCartBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        createOrderRequest= Gson().fromJson(intent.getStringExtra(Constants.ORDER_REQUEST),CreateOrderRequest::class.java)
        mBinding.apply {
            rvItemList.adapter= ProductBookingCartAdapter(this@Product_booking_cart_activity,createOrderRequest.products!!)
            rvItemList.layoutManager= LinearLayoutManager(this@Product_booking_cart_activity)
            btnBook.setOnClickListener { startActivity(Intent(this@Product_booking_cart_activity,Product_booking_slots_activity::class.java)) }
        }
    }
}