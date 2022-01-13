package com.rikkei.tranning.basekotlin

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class LocaleHelper {
    @RequiresApi(Build.VERSION_CODES.N)
    fun setLocale(context: Context?, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context?.resources?.updateConfiguration(config, context.resources.displayMetrics)
        CommonUtils.instance?.savePref("MyLang", lang)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadLocale(context: Context?) {
        val a = CommonUtils.instance?.getPref("MyLang")
        a?.let { setLocale(context, it) }
    }
}