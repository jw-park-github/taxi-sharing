package com.example.prj1114

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.prj1114.common.*
import com.example.prj1114.databinding.Act2SearchBinding
import com.example.prj1114.search.JusoFragment
import com.example.prj1114.search.ListFragment
import com.example.prj1114.search.SearchFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.navercorp.nid.NaverIdLoginSDK
import java.util.*

class Act02Search : AppCompatActivity(){
    private lateinit var binding:Act2SearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Act2SearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyAYNldNQ8vUsUrja-aj7UDOVXddngDiMsM", Locale.KOREA)
        }

        openSearchFragment()
    }

    fun openSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, Act03Map())
            .commit()
    }

    fun openJusoFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, JusoFragment())
            .commit()
    }

    fun openListFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_frame, ListFragment())
            .commit()
    }

//    fun replaceFragment(bundle:Bundle){
//        Log.d(TAG,"replaceFragment:${bundle.toString()}")
//        val destination = arrayListOf<Fragment>(SearchFragment(),JusoFragment(), ListFragment())
//        destination[bundle.getInt("dest")].arguments = bundle
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.layout_frame, destination[bundle.getInt("dest")])
//            .commit()
//    }
}