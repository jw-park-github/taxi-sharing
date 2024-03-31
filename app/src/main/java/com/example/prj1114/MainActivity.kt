package com.example.prj1114

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.prj1114.detail.ChatFragment
import com.example.prj1114.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, MainFragment())
            .commit()
    }

    fun replaceFragment(dest:Int){
        val destination = arrayListOf<Fragment>(MainFragment(),ChatFragment())
        val bundle = Bundle()
        bundle.putString("visited","Y")
        destination[dest].arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, destination[dest])
            .commit()
    }
}