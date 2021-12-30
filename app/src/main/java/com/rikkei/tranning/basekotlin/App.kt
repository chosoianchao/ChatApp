package com.rikkei.tranning.basekotlin

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {
        private var instance: App? = null
        fun getInstance(): App? {
            return instance
        }
    }
}