package com.example.prj1114.viewmodel


import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.example.prj1114.data.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.Calendar


class SearchViewModel :
    ViewModel(),
    OnMapReadyCallback,
    Listener {
    /** dependencies */
    private val repository = Repository()
    lateinit var placeClient: PlacesClient
    lateinit var googleMap: GoogleMap
    lateinit var easyWayLocation: EasyWayLocation
    lateinit var geocoder: Geocoder

    /** ## From Model to View
     * 데이터베이스에서 가져와서 ui 구성하기 위한 데이터입니다.
     * * autocompleteList: LiveData<List<AutocompletePrediction>> 자동완성 결과 list
     * * searchList: LiveData<List<Team>> 검색 결과 list
     */
    val autocompleteList: LiveData<List<AutocompletePrediction>> = liveData(Dispatchers.IO){
        fetchAutocomplete().asLiveData()
    }


    val searchList = liveData<List<Team>> {

    }

    /** ## From View to Model
     * Team() 객체를 구성하기 위한 모든 프로퍼티를 관찰합니다.
     * Team.curr 는 Team 생성 시 무조건 1로 설정됩니다.
     * * **date** from datePickerDialog
     * * time from timePickerDialog
     * * start from onClickListener of Recycler View
     * * end from onClickListener of Recycler View
     * * max from editText
     *
     * 다음 데이터는 실제 저장되는 데이터가 아닌
     * ui 구성을 위해 live 하게 추적해야 하는 데이터입니다.
     * * currLocation from onChangeListener of google map
     * * keyword from editText of start(or end)
     * * typingMode 현재 사용자가 typing 중인 검색 필드 구분
     */

    private val now: Calendar = Calendar.getInstance()

    private var _date = MutableLiveData(
        Triple(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
    )
    private var _time = MutableLiveData(
        Pair(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
    )
    private var _start = MutableLiveData<Address>()
    private var _end = MutableLiveData<Address>()
    private var _status = MutableLiveData<Int>()
    private var _max = MutableLiveData<Int>()

    val date: LiveData<Triple<Int, Int, Int>> get() = _date
    val time: LiveData<Pair<Int, Int>> get() = _time
    val start: LiveData<Address> get() = _start
    val end: LiveData<Address> get() = _end
    val status: LiveData<Int> get() = _status
    val max: LiveData<Int> get() = _max

    private var _keyword = MutableLiveData<String>()
    private var _typeMode = MutableLiveData<TypeMode>()
    val keyword: LiveData<String> get() = _keyword
    val typeMode: LiveData<TypeMode> get() = _typeMode

    private var chkPermission = MutableLiveData<Boolean>()
    private var chkListener = MutableLiveData(true)
    fun setChkListener(prop: Boolean) = run { chkListener.value = prop }
    fun setChkPermission(prop: Boolean) = run { chkPermission.value = prop }


    fun setKeyword(km: String) {
        _keyword.value = km
    }

    /** set date value */
    fun setDate(year: Int, month: Int, day: Int) {
        _date.value = Triple(year, month, day)
    }

    /** set time value */
    fun setTime(hour: Int, minute: Int) {
        _time.value = Pair(hour, minute)
    }


    /** ## Google Place Autocomplete 으로부터 데이터 fetch
     *  locationBias 서울시
     *  keyword 값을 기준으로 autocomplete 실행
     */
    private suspend fun fetchAutocomplete() = callbackFlow {
        if(keyword.value == null) close()

        val token = AutocompleteSessionToken.newInstance()
        val seoul = RectangularBounds.newInstance(
            LatLng(37.11604911395385, 127.48272601788462),
            LatLng(37.71488415371418, 126.82097381726436)
        )
        val types = listOf(TypeFilter.ADDRESS.toString(), TypeFilter.ESTABLISHMENT.toString())
        val request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(seoul)
            .setCountry("KR")
            // .setTypesFilter(types)
            .setSessionToken(token)
            .setQuery(keyword.value)
            .build()

        placeClient.findAutocompletePredictions(request)
            .addOnSuccessListener { res: FindAutocompletePredictionsResponse ->
                trySend(res.autocompletePredictions)
            }
            .addOnFailureListener { exp: Exception ->
                if(exp is ApiException) Log.e("APPLE", "Place not found $exp")
                close()
            }

        awaitClose {
            Log.d("APPLE", "Close fetchAutocomplete")
        }
    }

    /** ## OnMapCallBackListener 상속 메소드
     *  onMapReady
     *  setListener()로 리스너 설치,
     *  removeLister()로 기존 리스너 제거
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setListener()
    }

    private fun setListener() {
        googleMap.setOnCameraIdleListener {
            if(chkListener.value!!){
                googleMap.cameraPosition.target.let { latlng ->
                    val addressList = geocoder.getFromLocation(
                        latlng.latitude,
                        latlng.longitude, 1
                    )
                    addressList?.let { it ->
                        if (it.isNotEmpty()) {
                            val new = Address(
                                lat = latlng.latitude,
                                long = latlng.longitude,
                                name = it[0].featureName ?: "404",
                                address = it[0].getAddressLine(0) ?: "Default",
                            )
                            _start.value = new
                        }
                    }
                }
            }
        }
    }

    /** ## easyWayLocation Listener 상속 함수
     *  locationOn(), currentLocation(), locationCanceled()
     */
    override fun locationOn() {
        TODO("Not yet implemented")
    }
    override fun currentLocation(location: Location) {
        val sogangLatLng = LatLng(37.55090839220251, 126.94020282660945)
        val myLocation = LatLng(location.latitude, location.longitude)

        if (chkPermission.value!!) {
            googleMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(myLocation).zoom(15f).build()
                )
            )
        } else {
            googleMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(sogangLatLng).zoom(15f).build()
                )
            )
        }
    }
    override fun locationCancelled() {
        TODO("Not yet implemented")
    }

    /** clear easyWayLocation */
    override fun onCleared() {
        super.onCleared()
        easyWayLocation.endUpdates()
    }
}

