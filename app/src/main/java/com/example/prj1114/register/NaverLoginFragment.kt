package com.example.prj1114.register

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.common.MyApplication
import com.example.prj1114.databinding.FragmentNaverLoginBinding
import com.example.prj1114.viewmodel.RegisterViewModel
import com.navercorp.nid.NaverIdLoginSDK
import kotlinx.coroutines.launch

class NaverLoginFragment : Fragment() {
    private lateinit var binding: FragmentNaverLoginBinding
    private lateinit var viewmodel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNaverLoginBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val activity = context as RegisterActivity

        binding.naverLogin.setOnClickListener{
            lifecycleScope.launch{
                viewmodel.login(requireActivity()).let { userId ->
                    if(userId != null) {
                        Log.d("APPLE", "네이버 로그인 아이디가 DB에 존재합니다. $userId")
                        MyApplication.INSTANCE.setUserIdOnSharedPref(userId)
                        activity.recreate()
                    }
                    else activity.emailFragment()
                }
            }
        }
    }
}