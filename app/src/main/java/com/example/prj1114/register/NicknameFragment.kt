package com.example.prj1114.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.common.MyApplication
import com.example.prj1114.databinding.FragmentNicknameBinding
import com.example.prj1114.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

class NicknameFragment : Fragment() {
    private lateinit var binding: FragmentNicknameBinding
    private lateinit var viewmodel: RegisterViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        sharedPref = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNicknameBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val activity = context as RegisterActivity
        binding.regisBtn.isEnabled = false

        binding.nicknameText.editText?.addTextChangedListener {
            if(it?.toString()?.chkNicknameLength() == false) {
                binding.nicknameText.error = "3자 이상 20자 미만 한글, 영문, 숫자로 입력해 주세요."
            }
            else if(it?.toString()?.chkUniqueNickname() == false) {
                binding.nicknameText.error = "이미 존재하는 닉네임입니다."
            }
            else {
                binding.nicknameText.helperText = "사용 가능한 닉네임입니다."
                binding.regisBtn.isEnabled = true
            }
        }

        binding.regisBtn.setOnClickListener {
            val nickname = binding.nicknameText.editText?.text.toString()
            lifecycleScope.launch {
                viewmodel.setNickname(nickname)
                if(!viewmodel.isUniqueNickname()) {
                    binding.nicknameText.error = "이미 존재하는 닉네임입니다"
                } else {
                    viewmodel.saveUser()
                    MyApplication.INSTANCE.setUserIdOnSharedPref(viewmodel.userId.value!!)
                    activity.recreate()
                }
            }
        }
    }

    /** check valid nickname */
    private fun String.chkUniqueNickname(): Boolean {
        return true
    }

    private fun String.chkNicknameLength(): Boolean {
        /** nickname must be eng, kor, or number */
        val nickNameRegex = Regex("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]+\$")

        /** nickname length must be more than 4, less than 19 */
        return (this.length in 4..19) && (nickNameRegex.matches(this))
    }

    private fun putSharedPref(id: String) {
        with(sharedPref.edit()) {
            putString("id", id)
            apply()
        }
    }
}