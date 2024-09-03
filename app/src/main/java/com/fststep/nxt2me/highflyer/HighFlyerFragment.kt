package com.fststep.nxt2me.highflyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.fststep.nxt2me.R
import com.fststep.nxt2me.core.data.Preferences
import com.fststep.nxt2me.dashboard.view.fragment.HighFlyerFaqFragment
import com.fststep.nxt2me.databinding.FragmentHighFlyerBinding
import com.fststep.nxt2me.highflyer.dashboard.HfDashboardFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Shubham Singh on 01/08/21.
 */

@AndroidEntryPoint
class HighFlyerFragment: Fragment() {
    
    private lateinit var mBinding: FragmentHighFlyerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHighFlyerBinding.inflate(inflater, container, false)
        return  mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Preferences.fetchUser()?.highFlyerDto==null)
            childFragmentManager.beginTransaction().replace(mBinding.frag.id,HighFlyerFaqFragment()).commit()
        else
            childFragmentManager.beginTransaction().replace(R.id.frag, HfDashboardFragment()).commit()
    }
}