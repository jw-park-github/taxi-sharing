package com.example.prj1114

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.common.MyApplication
import com.example.prj1114.data.Repository
import com.example.prj1114.register.RegisterActivity
import com.example.prj1114.databinding.MainFragmentBinding
import com.example.prj1114.util.NodeServer
import com.example.prj1114.util.myCallProfileApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var application: MyApplication
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        repository = Repository()
        application = MyApplication.INSTANCE

        initEventListener()

        return binding.root
    }

    private fun initEventListener() {
        binding.scenario1.text = "로그인된 기존 사용자"
        binding.scenario2.text = "로그인되지 않은 기존 사용자"
        binding.scenario3.text = "앱을 처음 사용하는 신규 사용자"

        binding.button1.text = "검색 또는 생성"
        binding.button2.text = "상세화면 조회 및 채팅"

        binding.scenario1.setOnClickListener {
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.scenario2.setOnClickListener {
            NaverIdLoginSDK.logout()
            application.setUserIdOnSharedPref(null)

            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.scenario3.setOnClickListener {
            lifecycleScope.launch {
                val currUser = MyApplication.INSTANCE.getUserIdFromSharedPref()
                if(currUser != null) repository.deleteUser(currUser)
                application.setUserIdOnSharedPref(null)
                NidOAuthLogin().myCallProfileApi()

                val intent = Intent(activity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        binding.button1.setOnClickListener {
//            var myToken: String? = null
//            Firebase.messaging.token.addOnCompleteListener {task ->
//                if(!task.isSuccessful) {
//                    return@addOnCompleteListener
//                }
//                myToken = task.result
//                nodeServer.fcm(arrayOf(myToken!!))
//            }
            val intent = Intent(activity, Act02Search::class.java)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(activity, Act06Detail::class.java)
            startActivity(intent)
        }
    }

    fun callFragment(destination:Int){
        (activity as MainActivity).replaceFragment(destination)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
