package com.example.prj1114.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.prj1114.common.MyApplication
import com.example.prj1114.data.Repository
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun NaverIdLoginSDK.myInitialize(context: Context) {
    val clientId = "gr3SF8Z7Xn4PJYc5pFUp"
    val clientSecret = "5BRRhSf84i"
    val clientName = "PRJ1114"

    NaverIdLoginSDK.apply {
        showDevelopersLog(true)
        initialize(context, clientId, clientSecret, clientName)
        behavior = NidOAuthBehavior.CUSTOMTABS
        isShowMarketLink = true
        isShowBottomTab = true
    }
}

suspend fun NaverIdLoginSDK.myAuthenticate(
    context: Context,
    application: MyApplication,
    repository: Repository
): Boolean {
    val tag = "APPLE/Authenticate"
    return suspendCoroutine { continuation ->
        NaverIdLoginSDK.authenticate(context, object: OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken = NaverIdLoginSDK.getAccessToken()
                Log.d(tag, "Naver Token: $accessToken")
                continuation.resume(true)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.w(tag, "error code: $errorCode, $errorDescription")
                continuation.resume(false)
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}

suspend fun NidOAuthLogin.myCallProfileApi(): HashMap<String, Any?>? {
    val tag = "APPLE/NidOAuthLogin"

    return suspendCoroutine { continuation ->
        this.callProfileApi(object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                Log.d(tag, "user info : ${result.profile}")
                result.profile?.let {
                    val userId = it.id
                    val gender = it.gender
                    continuation.resume(hashMapOf(
                        "userId" to userId,
                        "gender" to gender
                    ))
                }
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.w(tag, "error code: $errorCode, $errorDescription")
                continuation.resume(null)
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}

suspend fun NidOAuthLogin.myDelete(context: Context): Boolean {
    val tag = "APPLE/DeleteLogin"

    return suspendCoroutine { continuation ->
        NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                continuation.resume(true)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d(tag, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d(tag, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                continuation.resume(false)
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }
}
