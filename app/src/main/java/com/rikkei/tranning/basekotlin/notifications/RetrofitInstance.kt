package com.rikkei.tranning.basekotlin.notifications

import com.rikkei.tranning.basekotlin.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        val api: NotificationApis by lazy {
            retrofit.create(NotificationApis::class.java)
        }
    }
}