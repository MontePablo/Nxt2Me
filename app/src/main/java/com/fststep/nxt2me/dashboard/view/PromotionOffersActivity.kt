package com.fststep.nxt2me.dashboard.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fststep.nxt2me.R
import com.fststep.nxt2me.dashboard.model.PromotionOffersItemInfo
import com.fststep.nxt2me.dashboard.view.adapter.PromotionOffersListAdapter
import com.fststep.nxt2me.databinding.ActivityPromotionOffersBinding

class PromotionOffersActivity: AppCompatActivity() {

    private lateinit var mBinding:ActivityPromotionOffersBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var promotionOffersListAdapter: PromotionOffersListAdapter
    private lateinit var promotionOffersList: MutableList<PromotionOffersItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_promotion_offers)

        mBinding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }

        mBinding.btnCreateNewPromotion.setOnClickListener {
            startActivity(Intent(this,CreatePromotionActivity::class.java))
        }

        linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvPromotions.layoutManager = linearLayoutManager
        promotionOffersList = ArrayList()
        dummyData()
        promotionOffersListAdapter = PromotionOffersListAdapter(this,
            promotionOffersList as ArrayList<PromotionOffersItemInfo>
        )
        mBinding.rvPromotions.adapter = promotionOffersListAdapter
        mBinding.rvPromotions.addItemDecoration(SpaceItemDecoration(20))
    }

    private fun dummyData() {
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Get special disc upto 25%","On this festival season enjoy food with your family"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
        promotionOffersList.add(PromotionOffersItemInfo(R.drawable.ic_groceries,"Summer Hot Sale upto 50% disc","On all apparels and clothing"))
    }

    class SpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

}