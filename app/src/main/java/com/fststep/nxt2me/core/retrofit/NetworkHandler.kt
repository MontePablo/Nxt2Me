package com.fststep.nxt2me.core.retrofit

import android.content.Context
import com.fststep.nxt2me.core.extension.networkInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Shubham Singh on 26/06/21.
 */
/**
 * Injectable class which returns information about the network connection state.
 */
@Singleton
open class NetworkHandler @Inject constructor(@ApplicationContext private val context: Context) {
    open val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}