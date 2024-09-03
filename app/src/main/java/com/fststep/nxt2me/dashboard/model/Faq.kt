package com.fststep.nxt2me.dashboard.model

import android.graphics.Color


/**
 * Created by Shubham Singh on 15/08/21.
 */
data class Faq (
    var title: String,
    var clipContent: String,
    var content: String,
    var color: Int,
    var readMore: Boolean = false
)