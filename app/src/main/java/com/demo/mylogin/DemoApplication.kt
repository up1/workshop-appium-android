package com.demo.mylogin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}