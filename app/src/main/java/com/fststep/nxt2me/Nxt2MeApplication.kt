package com.fststep.nxt2me

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.fststep.nxt2me.core.data.Preferences
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Shubham Singh on 26/06/21.
 */

@HiltAndroidApp
class Nxt2MeApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Preferences.init(applicationContext)
    }
}