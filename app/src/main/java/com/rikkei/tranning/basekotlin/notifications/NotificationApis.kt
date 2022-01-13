package com.rikkei.tranning.basekotlin.notifications

import com.rikkei.tranning.basekotlin.Constant.Companion.CONTENT_TYPE
import com.rikkei.tranning.basekotlin.Constant.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApis {
    @Headers("Authorization: key=$SERVER_KEY", "Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}