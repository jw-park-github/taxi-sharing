package com.example.prj1114.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JusoService{
    @GET("addrlink/addrLinkApi.do?")
    fun getJuso(
        @Query("keyword") keyword:String,
        @Query("confmKey") confmKey:String
    ): Call<SearchJusoDto>
}

interface OnAdapterItemClickListener {
    fun onAdapterItemClickListener(position:Int)
}