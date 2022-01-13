package com.rikkei.tranning.basekotlin

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        locale = LocaleHelper()
        EmojiManager.install(GoogleEmojiProvider())
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {
        private var locale: LocaleHelper? = null
        private var instance: App? = null
        fun getInstance(): App? {
            return instance
        }
    }
}