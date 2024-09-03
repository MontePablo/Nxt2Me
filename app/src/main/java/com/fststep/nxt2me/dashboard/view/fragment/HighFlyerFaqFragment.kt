package com.fststep.nxt2me.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fststep.nxt2me.dashboard.model.Faq
import com.fststep.nxt2me.databinding.FragmentHighFlyerFaqBinding
import com.fststep.nxt2me.highflyer.FaqAdapter
import com.fststep.nxt2me.highflyer.registration.HighFlyerRegistration

class HighFlyerFaqFragment:Fragment() {

    private lateinit var mBinding: FragmentHighFlyerFaqBinding
    private lateinit var adapter: FaqAdapter

    private var launchHighFlyerReg = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mBinding = FragmentHighFlyerFaqBinding.inflate(layoutInflater, container, false)
        val highflyer: View = mBinding.root

        return highflyer
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rvFaq.layoutManager = LinearLayoutManager(requireContext())
        mBinding.rvFaq.setHasFixedSize(true)
        adapter = FaqAdapter(getFaqs())

        mBinding.rvFaq.adapter = adapter

        mBinding.btHighFlyerRegister.setOnClickListener {
            launchHighFlyerReg.launch(Intent(requireContext(), HighFlyerRegistration::class.java))
        }
    }

    private fun getFaqs(): ArrayList<Faq> = arrayListOf(
        Faq(
            "What do you mean by HIGH-Flyer?",
            "High-Flyer is the platform where individual can start their own business with minimal investm….",
            "High-Flyer is the platform where an individual can start their own business with minimal investment. If you are a teacher and want to start tuition, or if you are a housewife and want to start a tiffin service, or if you are a yoga trainer and want to start your yoga classes. Then this is the place.",
            Color.parseColor("#FFeb008b")
        ),
        Faq(
            "Who all can Register?",
            "There is no restriction, if you are passionate and willing to do something to improving your lifes….",
            "There is no restriction, if you are passionate and willing to do something to improve your lifestyle then this is the place. You should have skill and zeel to earn money.",
            Color.parseColor("#FF2e3092")
        ),
        Faq(
            "How does it Work?",
            "Our platform based on Geo fencing. It means you can define the area in which you want to d…",
            "Our platform is based on Geo fencing. It means you can define the area in which you want to start your business. People in our application can see your advertisement and service, if interested they will connect you through call or chat function.",
            Color.parseColor("#FF00a550")
        ),
        Faq(
            "How much do I need to Invest?",
            "You have to take monthly or yearly subscription. Initial investment can be as low as Rs. 100/pm…",
            "You have to take a monthly or yearly subscription. The initial investment can be as low as Rs. 100/pm or a maximum of Rs.300/pm based on the nature of your business.",
            Color.parseColor("#FFec1d24")
        ),
        Faq(
            "Why do I need to Share Application?",
            "When you share this application you will benefit yourself and others. With sharing we can onb….",
            "When you share this application you will benefit yourself and others. With sharing, we can onboard maximum people on our application and this will help you in increasing your business scope.",
            Color.parseColor("#FF0182b2")
        ),
    )

}