package com.example.prj1114.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prj1114.Act06Detail
import com.example.prj1114.common.CurrentInfo
import com.example.prj1114.databinding.DetailFragmentBinding
import com.example.prj1114.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.IOException

class DetailFragment :Fragment() {
    private var mbinding: DetailFragmentBinding? = null
    private val binding get() = mbinding!!

    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mbinding = DetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            viewModel.getTargetTeam()
            viewModel.setButton()
        }

        viewModel.isAwait.observe(viewLifecycleOwner) {
            when(it) {
                true -> loading()
                false -> show()
            }
        }

        viewModel.btnText.observe(viewLifecycleOwner) {
            when(it) {
                true -> binding.button.text = "view chat"
                false -> binding.button.text = "join team"
            }
        }

        /** navigate to chat */
        binding.button.setOnClickListener{
            when(viewModel.btnText.value) {
                true -> {
                    Log.d(CurrentInfo.TAG,"DF: button click true")
                    callFragment(1)
                }
                false -> lifecycleScope.launch {
                    Log.d(CurrentInfo.TAG,"DF: button click false")
                    viewModel.joinTargetTeam()
                    viewModel.setButton()
                }
                else -> {Log.d(CurrentInfo.TAG,"DetailFrag: ButtonEventError")}
            }
        }

        return view
    }

    private fun loading() {
        binding.loading.visibility = View.VISIBLE
    }
    private fun show() {
        binding.loading.visibility = View.GONE
        binding.timeText.text = viewModel.time
        binding.maxText.text = viewModel.max
        binding.startText.text = viewModel.start
        binding.endText.text = viewModel.end
    }
    private fun callFragment(dest: Int){
        (activity as Act06Detail).replaceFragment(dest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mbinding = null
    }
}