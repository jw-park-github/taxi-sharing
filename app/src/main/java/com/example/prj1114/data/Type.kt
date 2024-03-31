package com.example.prj1114.data

import retrofit2.Call
import retrofit2.http.*
import java.util.*

data class FCMRes(
    var result: String? = null
)

data class EmailRes(
    var status: String? = null,
    var info: String? = null,
    var number: Number? = null
)

enum class TypeMode {
    START, END
}

enum class WhereType {
    EQUAL, NOT_EQUAL,
    LESS_THAN, LESS_THAN_EQUAL,
    GREATER_THAN, GREATER_THAN_EQUAL,
    ARRAY_CONTAINS
}

data class Where(
    val type: WhereType = WhereType.EQUAL,
    val field: String = "",
    val value: Any = Object()
)

data class OrderBy(
    val field: String = "",
    val isAsc: Boolean = true
)

data class Address (
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val name: String = "",
    val address: String = ""
)

interface NodeServerInterface {
    @FormUrlEncoded
    @POST("/fcm")
    fun fcmPostRequest(
        @Field("title") title: String,
        @Field("body") body: String,
        @Field("tokens") tokens: Array<String>): Call<FCMRes>

    @FormUrlEncoded
    @POST("/mail")
    fun mailPostRequest(@Field("email") email: String): Call<EmailRes>
}