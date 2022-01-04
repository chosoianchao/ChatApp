package com.rikkei.tranning.basekotlin

import android.content.Context

class CommonUtils {
    fun savePref(key: String?, value: String?) {
        val pref = App.getInstance()!!
            .getSharedPreferences(FILE_PREF, Context.MODE_PRIVATE)
        pref.edit().putString(key, value).apply()
    }

    fun getPref(key: String?): String? {
        val pref = App.getInstance()!!
            .getSharedPreferences(FILE_PREF, Context.MODE_PRIVATE)
        return pref.getString(key, null)
    }

    companion object {
        private const val FILE_PREF = "file_pref"
        var instance: CommonUtils? = null
            get() {
                if (field == null) {
                    field = CommonUtils()
                }
                return field
            }
            private set
    }
}