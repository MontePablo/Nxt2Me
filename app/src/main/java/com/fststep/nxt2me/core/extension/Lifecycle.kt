package com.fststep.nxt2me.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.fststep.nxt2me.core.retrofit.exception.Failure

/**
 * Created by Shubham Singh on 26/06/21.
 */

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L?, body: (T?) -> Unit) =
    liveData?.observe(this, Observer(body))

fun <L : LiveData<Failure>> LifecycleOwner.failure(liveData: L, body: (Failure?) -> Unit) =
    liveData.observe(this, Observer(body))