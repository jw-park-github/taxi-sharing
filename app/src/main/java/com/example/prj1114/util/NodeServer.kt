package com.example.prj1114.util

import android.util.Log
import com.example.prj1114.data.EmailRes
import com.example.prj1114.data.FCMRes
import com.example.prj1114.data.NodeServerInterface
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class NodeServer {
    private val url = "http://3.38.24.214:3000"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val server = retrofit.create(NodeServerInterface::class.java)

    suspend fun sendEmail(email: String): String {
        val result: EmailRes = server.mailPostRequest(email).await()
        return result.number.toString()
    }

    fun fcm(tokens: Array<String>) {
        server.fcmPostRequest("sample", "푸시 알림 테스트", tokens)
            .enqueue(object : Callback<FCMRes> {
                override fun onFailure(call: Call<FCMRes>, t: Throwable) {
                    Log.w("APPLE/ FCM", "request fail", t)
                }
                override fun onResponse(call: Call<FCMRes>, response: Response<FCMRes>) {
                    Log.d("APPLE/ FCM", "request success $response")
                }
            })

    }
}