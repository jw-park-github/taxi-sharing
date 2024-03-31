package com.example.prj1114.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.prj1114.util.myInitialize
import com.navercorp.nid.NaverIdLoginSDK

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharedPref = this.getSharedPreferences("SharedPref", Context.MODE_PRIVATE)

        NaverIdLoginSDK.myInitialize(this@MyApplication)

        Log.d("APPLE", "어플리케이션 생성")
        Log.d("APPLE", "sharedPreference: ${sharedPref.all}")
    }

    fun myApplicationLogin(userId: String) {
        current_user_id = userId
    }

    fun myApplicationLogout() {
        current_user_id = null
    }

    fun getUserIdFromSharedPref(): String? {
        val result = sharedPref.getString("id", null)
        Log.d("APPLE", "SharedPref: " + sharedPref.all.toString())
        return result
    }

    fun setUserIdOnSharedPref(id: String?) {
        with(sharedPref.edit()) {
            putString("id", id)
            apply()
        }
        Log.d("APPLE", "SharedPref: " + sharedPref.all.toString())
    }

    companion object {
        var current_user_id: String? = null
        lateinit var sharedPref: SharedPreferences
        lateinit var INSTANCE: MyApplication
            private set
    }
}