package com.example.prj1114.register

import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.databinding.FragmentEmailBinding
import com.example.prj1114.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class EmailFragment : Fragment() {
    private lateinit var binding: FragmentEmailBinding
    private lateinit var viewmodel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailBinding.inflate(inflater, container, false)
        viewmodel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]

        init()

        return binding.root
    }

    private fun init() {
        val activity = context as RegisterActivity

        binding.emailBtn.isEnabled = false
        binding.codeBtn.isEnabled = false

        binding.codeText.visibility = View.GONE
        binding.codeBtn.visibility = View.GONE

        binding.emailText.editText?.addTextChangedListener {
            if(it?.toString()?.chkEmail() == false){
                binding.emailText.error = "유효한 이메일을 입력해 주세요"
            }
            else {
                binding.emailText.error = ""
                binding.emailBtn.isEnabled = true
            }
        }

        binding.emailBtn.setOnClickListener{
            val email = binding.emailText.editText?.text.toString()
            var isUnique: Boolean = false
            lifecycleScope.launch{
                viewmodel.setEmail(email)
                if(!viewmodel.isUniqueEmail()) {
                    binding.emailText.error = "이미 가입된 메일입니다"
                } else {
                    viewmodel.sendEmail()
                    startTimer()
                    showCode()
                }
            }
        }

        binding.codeText.editText?.addTextChangedListener {
            if(it?.toString()?.chkCode() == false) {
                binding.codeText.error = "정확한 인증 코드를 입력해 주세요"
            } else {
                binding.codeText.error = ""
                binding.codeBtn.isEnabled = true
            }
        }

        binding.codeBtn.setOnClickListener {
            Toast.makeText(context, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            activity.nicknameFragment()
        }
    }

    /** check valid email & code */
    private fun String.chkEmail(): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun String.chkUniEmail():Boolean{
        val uniEmailRegex = Regex("[a-zA-Z0-9._-]+@sogang.ac.kr")
        return uniEmailRegex.matches(this)
    }

    private fun String.chkCode(): Boolean {
        return this == viewmodel.code.value
    }

    /** check valid code time limit */
    private fun startTimer(){
        var time = 180
        timer(period = 1000) {
            if(time == 0) {
                onInvalidTime()
            } else {
                time--
                val min = time / 60
                val sec = time % 60
                onValidTime(min, sec)
            }
        }
    }

    private fun onValidTime(min: Int, sec: Int) {
        val handler: Handler = object: Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                binding.emailText.helperText = "3분 내 인증 코드를 입력해 주세요.  $min : $sec"
                binding.emailBtn.isEnabled = false
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    private fun onInvalidTime() {
        val handler: Handler = object: Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                binding.emailText.error = "인증 시간이 만료되었습니다. 재전송 버튼을 눌러주세요"
                binding.emailBtn.isEnabled = true
                binding.emailBtn.text = "재전송"
            }
        }
        handler.obtainMessage().sendToTarget()
    }

    /** update ui */
    private fun showCode() {
        binding.codeText.visibility = View.VISIBLE
        binding.codeBtn.visibility = View.VISIBLE
    }
}