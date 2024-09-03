package com.fststep.nxt2me.dashboard.helper

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class AutoSwitcherViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {

    private val mSwitcher: Runnable = object : Runnable {
        override fun run() {
            if (this@AutoSwitcherViewPager.adapter != null) {
                var count = this@AutoSwitcherViewPager.currentItem
                if (count == this@AutoSwitcherViewPager.adapter!!.count - 1) {
                    count = 0
                } else {
                    count++
                }
                this@AutoSwitcherViewPager.setCurrentItem(count, true)
            }
            this@AutoSwitcherViewPager.postDelayed(this, 5000)
        }
    }

    constructor(context: Context?) : this(context!!, null) {}

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        when (arg0.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> postDelayed(mSwitcher, 5000)
            else -> removeCallbacks(mSwitcher)
        }
        return super.onTouchEvent(arg0)
    }

    init {
        postDelayed(mSwitcher, 5000)
    }
}