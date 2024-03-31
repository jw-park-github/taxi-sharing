package com.example.prj1114.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.Act02Search
import com.example.prj1114.data.*
import com.example.prj1114.databinding.SearchFragmentBinding
import com.example.prj1114.viewmodel.SearchViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class SearchFragment :
    Fragment()
{
//    private var mbinding: SearchFragmentBinding? = null
//    private val binding get() = mbinding!!
//    private lateinit var dialogView: View
//    private lateinit var time:Timestamp
//    private lateinit var juso1:Juso
//    private lateinit var juso2:Juso
//    private val teamList = arrayListOf<Team>()
//
//    private lateinit var viewmodel: SearchViewModel
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mbinding = SearchFragmentBinding.inflate(inflater, container, false)
//        viewmodel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
//        val view = binding.root
//
//        init()
//
//        return view
//    }
//
//    private fun init() {
//        /** init pickers */
//        binding.datePicker.setOnDateChangedListener(this)
//        binding.timePicker.setOnTimeChangedListener(this)
//        binding.datePicker.minDate = viewmodel.now.timeInMillis
//
//        /** click event */
//        binding.btnDate.setOnClickListener {toggle(binding.datePicker, binding.timePicker)}
//        binding.btnTime.setOnClickListener {toggle(binding.timePicker, binding.datePicker)}
//        binding.btnStart.setOnClickListener {
//            viewmodel.setSearchingJusoOf("start")
//            callFragment(1)
//        }
//        binding.btnEnd.setOnClickListener {
//            viewmodel.setSearchingJusoOf("end")
//            callFragment(1)
//        }
//        binding.btnSearch.setOnClickListener {
////            Toast.makeText(context, "Show Search Result", Toast.LENGTH_SHORT).show()
////            lifecycleScope.launch{ viewmodel.saveTeam() }
//            callFragment(2)
//        }
//
//        /** set observer */
//        viewmodel.date.observe(viewLifecycleOwner){
//            binding.btnDate.text = viewmodel.getDate()
//        }
//        viewmodel.time.observe(viewLifecycleOwner){
//            binding.btnTime.text = viewmodel.getTime()
//        }
//        viewmodel.start.observe(viewLifecycleOwner) {
//            binding.btnStart.text = viewmodel.getStart()
//        }
//        viewmodel.end.observe(viewLifecycleOwner) {
//            binding.btnEnd.text = viewmodel.getEnd()
//        }
//    }
//
//    override fun onDateChanged(view: DatePicker?, year: Int, month: Int, day: Int) {
//        viewmodel.setDate(year, month, day)
//    }
//
//    override fun onTimeChanged(view: TimePicker?, hour: Int, minute: Int) {
//        viewmodel.setTime(hour, minute)
//    }
//
//    private fun callFragment(flag:Int){
//        if(flag==1) (activity as Act02Search).openJusoFragment()
//        else if(flag==2) (activity as Act02Search).openListFragment()
//    }
//
//    private fun toggle(open: View, close: View) {
//        when(open.visibility) {
//            View.VISIBLE -> closePicker(open)
//            View.GONE -> {
//                closePicker(close)
//                openPicker(open)
//            }
//            View.INVISIBLE -> {
//                closePicker(close)
//                openPicker(open)
//            }
//        }
//    }
//    private fun openPicker(view: View) {
//        view.visibility = View.VISIBLE
//    }
//    private fun closePicker(view: View) {
//        view.visibility = View.GONE
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mbinding = null
//    }
}