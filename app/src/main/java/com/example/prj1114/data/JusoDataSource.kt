package com.example.prj1114.data

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit
import java.io.Serializable

//Xml POJO
@Xml(name = "common")
data class Common(
    @PropertyElement(name = "totalCount")
    val totalCount: String,
    @PropertyElement(name = "errorCode")
    val errorCode: String
)

@Xml(name = "juso")
data class Juso(
    @PropertyElement(name = "roadAddr")
    val roadAddr: String,
    @PropertyElement(name = "engAddr")
    val engAddr: String,
    @PropertyElement(name = "zipNo")
    val zipNo: String,
    @PropertyElement(name = "bdNm")
    val bdNm: String,
    @PropertyElement(name = "siNm")
    val siNm: String,
    @PropertyElement(name = "sggNm")
    val sggNm: String,
    @PropertyElement(name = "emdNm")
    val emdNm: String
) : Serializable {
    fun asString(): String {
        val strBuilder = StringBuilder()
            .append(this.siNm).append(" ")
            .append(this.sggNm).append(" ")
            .append(this.emdNm)
        return strBuilder.toString()
    }
}

data class Juso2(
    val name: String,
    val lat: Double,
    val lng:Double
)

@Xml(name = "response")
data class SearchJusoDto(
    @Element(name = "common")
    val common: Common,
    @Element(name = "juso")
    val juso: List<Juso>
)

object JusoInit {
    const val confmKey:String = "U01TX0FVVEgyMDIyMTExNDE2NDg0NTExMzIxOTg="
}

// "https://business.juso.go.kr/addrlink/addrLinkApi.do"
object RetrofitClient {
    fun getXMLInstance(): Retrofit {
        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        return Retrofit.Builder()
            .baseUrl("https://business.juso.go.kr/")
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .build()
    }
}