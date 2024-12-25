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
import com.fststep.nxt2me.dashboard.view.adapter.ProductGoodsCartAdapter
import com.fststep.nxt2me.databinding.ActivityProductGoodsCartBinding
import com.google.gson.Gson

class Product_goods_cart_activity : AppCompatActivity(),GoodsCartListener {
    lateinit var mBinding:ActivityProductGoodsCartBinding
    lateinit var createOrderRequest: CreateOrderRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityProductGoodsCartBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        createOrderRequest= Gson().fromJson(intent.getStringExtra(Constants.ORDER_REQUEST),CreateOrderRequest::class.java)
        mBinding.apply {
            rvItemList.adapter= ProductGoodsCartAdapter(this@Product_goods_cart_activity,createOrderRequest.products?: listOf())
            rvItemList.layoutManager=LinearLayoutManager(this@Product_goods_cart_activity)
            btnPay.setOnClickListener { startActivity(Intent(this@Product_goods_cart_activity,PaymentSuccessActivity::class.java)) }
        }
    }

    override fun increaseQuantity(product: Product) {
        product.quantity++
    }

    override fun decreaseQuantity(product: Product) {
        product.quantity--
    }

    override fun deleteProduct(product: Product) {
        createOrderRequest.products?.remove(product)
    }
}
interface GoodsCartListener{
    fun increaseQuantity(product: Product)
    fun decreaseQuantity(product: Product)
    fun deleteProduct(product: Product)
}