package com.example.j

import android.app.Application
import com.google.android.gms.ads.MobileAds

class AppClass : Application() {
    override fun onCreate() {
        MobileAds.initialize(
            this
        ) { }


        super.onCreate()
    }

}