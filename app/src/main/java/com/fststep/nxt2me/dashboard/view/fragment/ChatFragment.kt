package com.fststep.nxt2me.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fststep.nxt2me.databinding.FragmentChatBinding

class ChatFragment:Fragment() {

    private lateinit var mBinding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        mBinding = FragmentChatBinding.inflate(layoutInflater)
        val chat: View = mBinding.root

        return chat
    }

}