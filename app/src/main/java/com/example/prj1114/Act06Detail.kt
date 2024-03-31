package com.example.prj1114

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.prj1114.detail.ChatFragment
import com.example.prj1114.databinding.Act6ArticleBinding
import com.example.prj1114.detail.DetailFragment
import com.example.prj1114.viewmodel.DetailViewModel

class Act06Detail : AppCompatActivity(){
    private lateinit var binding: Act6ArticleBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act6ArticleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        openDetailFragment()
    }

    private fun openDetailFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, DetailFragment())
            .commit()
    }

    private fun openChatFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, ChatFragment())
            .commit()
    }

    fun replaceFragment(dest:Int){
        val destination = arrayListOf<Fragment>(DetailFragment(),ChatFragment())
//        val bundle = Bundle()
//        bundle.putString("visited","Y")
//        destination[dest].arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, destination[dest])
            .commit()
    }
}