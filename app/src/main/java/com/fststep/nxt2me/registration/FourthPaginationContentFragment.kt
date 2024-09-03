package com.fststep.nxt2me.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fststep.nxt2me.R

/**
 * Created by Jay Kulshreshtha on 27/06/21.
 */
class FourthPaginationContentFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fourth_pagination_content, container, false)
    }

}