package com.example.prj1114.register

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.Act02Search
import com.example.prj1114.R
import com.example.prj1114.common.MyApplication
import com.example.prj1114.data.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        repository = Repository()

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        Log.d("APPLE", network.toString())
        if(network == null) {
            Toast.makeText(this, "인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show()
        } else {
            autoLogin()
        }
    }

    private fun autoLogin() {
        /** 자동 로그인 로직 */
        MyApplication.INSTANCE.getUserIdFromSharedPref().let { userId ->
            when(userId){
                null -> {
                    // 저장된 로그인 정보 없음. 로그인 필요
                    Log.d("APPLE", "저장된 로그인 정보가 없습니다.")
                    naverLoginFragment()
                }
                else -> {
                    // 저장된 유저 아이디가 실제 DB에 존재하는지 체크
                    lifecycleScope.launch {
                        val result = repository.chkUserById(userId)
                        if(!result){
                            MyApplication.current_user_id = userId
                            Log.d("APPLE", "저장된 유저 아이디가 DB에 존재합니다. $userId")
                            Log.d("APPLE", "저장된 유저 아이디로 어플리케이션에 로그인. ${MyApplication.current_user_id}")

                            searchActivity()
                        }
                        else {
                            Log.d("APPLE", "저장된 유저 아이디가 DB에 존재하지 않습니다.")
                            naverLoginFragment()
                        }
                    }
                }
            }
        }
    }

    private fun naverLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, NaverLoginFragment())
            .commit()
    }
    fun emailFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, EmailFragment())
            .commit()
    }
    fun nicknameFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, NicknameFragment())
            .commit()
    }
    fun searchActivity() {
        val intent = Intent(this@RegisterActivity, Act02Search::class.java)
        startActivity(intent)
    }
}