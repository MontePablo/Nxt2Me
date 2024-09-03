/* Copyright (C) 2021 Fststep Next2Me - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.fststep.nxt2me.highflyer.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fststep.nxt2me.R
import com.fststep.nxt2me.databinding.FragmentEarningsBinding
import com.fststep.nxt2me.highflyer.registration.ServiceType
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

/**
 * Created by Jay Kulshreshtha on 12/06/21.
 */
class EarningsFragment : Fragment() {

    private var serviceType: ServiceType? = null

    private lateinit var mBinding: FragmentEarningsBinding
    private lateinit var pieChart: PieChart
    private lateinit var earning: MutableList<PieEntry>

    companion object {
        fun newInstance(serviceType: ServiceType): EarningsFragment {
            val frag = EarningsFragment()
            frag.serviceType = serviceType
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mBinding = FragmentEarningsBinding.inflate(layoutInflater)
        val earnings: View = mBinding.root

        pieChart = mBinding.pieChart
        earning = ArrayList()
        earning.add(PieEntry(18000f, 0f))
        earning.add(PieEntry(6000f, 1f))
        val dataSet = PieDataSet(earning, "")
        val data = PieData(dataSet)
        data.setValueTextColor(Color.parseColor("#ffffff"))
        pieChart.data = data
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.accepted_earnings_color),
            ContextCompat.getColor(requireContext(), R.color.rejected_earnings_color)
        )
        pieChart.animateXY(0, 0)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.holeRadius = 70f

        return earnings
    }
}
