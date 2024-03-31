package com.example.prj1114.viewmodel

import androidx.lifecycle.ViewModel
import com.example.prj1114.data.User
import com.example.prj1114.util.NodeServer
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.prj1114.common.MyApplication
import com.example.prj1114.data.Repository
import com.example.prj1114.util.myAuthenticate
import com.example.prj1114.util.myCallProfileApi
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin

class RegisterViewModel : ViewModel() {
    private val tag = "APPLE/ RegisterViewModel"

    private lateinit var nodeServer: NodeServer
    private val repository = Repository()

    /** livedata */
    private var _userId = MutableLiveData<String>()
    private var _gender = MutableLiveData<String>()
    private var _nickname = MutableLiveData<String>()
    private var _email = MutableLiveData<String>()
    private var _code = MutableLiveData<String>()

    val userId: LiveData<String> get() = _userId
    val gender: LiveData<String> get() = _gender
    val nickname: LiveData<String> get() = _nickname
    val email: LiveData<String> get() = _email
    val code: LiveData<String> get() = _code

    fun setEmail(email:String) {
        _email.value = email
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    suspend fun login(context: Context): String? {
        val application = MyApplication()
        NaverIdLoginSDK.myAuthenticate(context, application, repository)
        NidOAuthLogin().myCallProfileApi().let {
            _userId.value = it?.get("userId") as String
            _gender.value = it["gender"] as String
        }

        repository.chkUserByField(Pair("userId", userId.value!!)).let {
            if(it) return userId.value
            else return null
        }
    }

    suspend fun sendEmail(){
        nodeServer = NodeServer()
        email.value?.let {
            _code.value = nodeServer.sendEmail(it)
        }
    }

    suspend fun saveUser(){
        Log.d(tag, "$userId, $gender, $nickname, $email")
        if(chkNull()) {
            repository.createUser(
                User(userId = userId.value!!,
                    gender = gender.value!!,
                    email = email.value!!,
                    nickname = nickname.value!!
                ))
        } else {
            TODO()
        }
    }

    suspend fun isUniqueNickname(): Boolean {
        return repository.chkUserByField(Pair("nickname", email.value!!))
    }

    suspend fun isUniqueEmail(): Boolean {
        return repository.chkUserByField(Pair("email", email.value!!))
    }

    private fun chkNull(): Boolean {
        return (userId.value != null) &&
                (gender.value != null) &&
                (email.value != null) &&
                (nickname.value != null)
    }
}