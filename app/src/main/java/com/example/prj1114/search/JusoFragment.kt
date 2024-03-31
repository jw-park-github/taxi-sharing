package com.example.prj1114.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prj1114.Act02Search
import com.example.prj1114.databinding.JusoFragmentBinding
import com.example.prj1114.data.*
import com.example.prj1114.viewmodel.SearchViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class JusoFragment : Fragment()/*, OnAdapterItemClickListener*/ {
//    private var mbinding:JusoFragmentBinding? = null
//    private val binding get() = mbinding!!
//    private var jusoList = arrayListOf<Juso>()
//    private lateinit var keyword:String
//    private lateinit var adapter:JusoAdapter
//    private lateinit var viewmodel: SearchViewModel
//    private lateinit var retrofit: Retrofit
//    private lateinit var jusoService: JusoService
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mbinding = JusoFragmentBinding.inflate(inflater,container,false)
//        viewmodel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
//        val view = binding.root
//
//        retrofit = RetrofitClient.getXMLInstance()
//        jusoService = retrofit.create(JusoService::class.java)
//
//        binding.jusoRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        adapter = JusoAdapter(jusoList,this)
//        binding.jusoRecyclerview.adapter = adapter
//
//        /** observe */
//        viewmodel.keyword.observe(viewLifecycleOwner){}
//        viewmodel.start.observe(viewLifecycleOwner){}
//        viewmodel.end.observe(viewLifecycleOwner){}
//
//        binding.editText.addTextChangedListener {
//            viewmodel.setKeyword(it.toString())
//            binding.btnSend.isEnabled = it.toString().isNotEmpty()
//        }
//
//        /** get juso */
//        binding.btnSend.setOnClickListener {
//            /** TODO: liveData pattern으로 바꾸기 */
//            val keyword = binding.editText.text.toString()
//            jusoService.getJuso(keyword, JusoInit.confmKey)
//                .enqueue(object : Callback<SearchJusoDto> {
//                    override fun onFailure(call: Call<SearchJusoDto>, t: Throwable) {
//                        Log.w("APPLE/", "error occurred with api")
//                    }
//
//                    override fun onResponse(
//                        call: Call<SearchJusoDto>,
//                        response: Response<SearchJusoDto>
//                    ) {
//                        response.body()?.let {
//                            it.juso.forEach { juso ->
//                                jusoList.add(juso)
//                            }
//                        }
//                        adapter.notifyDataSetChanged()
//                    }
//                })
//        }
//
//        return view
//    }
//
//    override fun onAdapterItemClickListener(position: Int) {
//        viewmodel.setJuso(jusoList[position])
//        callFragment()
//    }
//
//    private fun callFragment(){
//        (activity as Act02Search).openSearchFragment()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mbinding = null
//    }
}