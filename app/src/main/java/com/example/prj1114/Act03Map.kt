package com.example.prj1114

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.easywaylocation.EasyWayLocation
import com.example.prj1114.databinding.ActivityMapBinding
import com.example.prj1114.viewmodel.SearchViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.chat_fragment.view.*
import java.util.*

class Act03Map : Fragment() {
    private lateinit var viewmodel: SearchViewModel
    private lateinit var binding: ActivityMapBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        initViewModel()
        initLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mymap) as SupportMapFragment
        mapFragment.getMapAsync(viewmodel)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.buttomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight = 100

        /** ## liveData 를 변경하는 이벤트 리스너 설치 */
        binding.startText.setOnClickListener {
            viewmodel.setChkListener(false) // 위치 검색을 이용하지 않습니다.
            viewmodel.start.removeObservers(viewLifecycleOwner)

            it.editText?.text?.clear()
            bottomSheetBehavior.peekHeight = 200
        }
        binding.startText.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewmodel.setKeyword(it.toString())
            }
        }

        /** ## liveData 를 관찰하는 옵저버 설치 */
        viewmodel.start.observe(viewLifecycleOwner) {
            binding.startText.setText(it.address)
        }
        viewmodel.keyword.observe(viewLifecycleOwner) {
            // viewmodel.fetchAutocomplete()
        }
        viewmodel.autocompleteList.observe(viewLifecycleOwner) {
            Log.d("APPLE", "${viewmodel.autocompleteList.value}")
        }

        return binding.root
    }

    /** viewmodel 에서 사용하는 class 중
     *  activity context 가 필요한 class 초기화
     */
    private fun initViewModel() {

        val locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = Priority.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 1f
        }

        viewmodel.easyWayLocation = EasyWayLocation(
            requireActivity(),
            locationRequest,
            false,
            false,
            viewmodel
        )

        viewmodel.easyWayLocation.startLocation()
        viewmodel.geocoder = Geocoder(requireActivity(), Locale("KR"))
        viewmodel.setChkPermission(chkPermission())
        viewmodel.placeClient = Places.createClient(requireActivity())
    }

    private fun chkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initLocationPermission() {
        val locationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            when {
                permission.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d("LOCATIONS", "승인")
                    //easyWayLocation.startLocation()
                }
                permission.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d("LOCATIONS", "부분 승인")
                    //easyWayLocation.startLocation()
                }
                else -> {
                    Log.d("LOCATIONS", "거부")
                }
            }

        }

        locationPermissions.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }
}