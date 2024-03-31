package com.example.prj1114.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prj1114.Act02Search
import com.example.prj1114.common.*
import com.example.prj1114.data.OnAdapterItemClickListener
import com.example.prj1114.data.Team
import com.example.prj1114.databinding.ListFragmentBinding
import com.example.prj1114.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ListFragment : Fragment()/*, OnAdapterItemClickListener */{
    private var mbinding:ListFragmentBinding? = null
    private val binding get() = mbinding!!
    private var teamList = arrayListOf<Team>()
    private lateinit var adapter:ListAdapter
    private lateinit var viewmodel:SearchViewModel
    private lateinit var compare:Team

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mbinding = ListFragmentBinding.inflate(inflater,container,false)
        viewmodel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        val view = binding.root

        binding.listRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        // adapter = ListAdapter(teamList,this)
        binding.listRecyclerview.adapter = adapter

        return view
    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mbinding = ListFragmentBinding.inflate(inflater,container,false)
//        val view = binding.root
//
//        compare = arguments?.getSerializable("team",Team::class.java)!!
//
//        binding.listRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        adapter = ListAdapter(teamList,this)
//        binding.listRecyclerview.adapter = adapter
//
//
//        return view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        CoroutineScope(Dispatchers.Default).launch {
//            teamList = viewmodel.get()
//            for(team in teamList) {
//                if(team.teamId == "") continue
//                else if(filteringTeam(team)){
//                    teamList.remove(team)
//                    continue
//                }
//                else if(filteringTeam(team, Pair("start",viewmodel.getStart()!!))) {
//                    teamList.remove(team)
//                    continue
//                }
//                else if(filteringTeam(team, Pair("end",viewmodel.getEnd()!!))) {
//                    teamList.remove(team)
//                    continue
//                }
//            }
//            teamList.sortBy { it.time }
//            adapter.notifyDataSetChanged()
//        }
//        db.collection("Team").orderBy("time",Query.Direction.DESCENDING)
//            .limit(50).get()
//            .addOnSuccessListener { documents ->
//                for(document in documents){
//                    Log.d(TAG,"document: ${document}")
//                    val item = Team(
//                        document["teamId"] as String?,
//                        document["time"] as Timestamp?,
//                        Juso(document["start"] as Map<String, String>),
//                        Juso(document["end"] as Map<String, String>),
//                        Integer.parseInt(document["status"].toString()),
//                        Integer.parseInt(document["limit"].toString()),
//                        Integer.parseInt(document["cur"].toString()),
//                        document["users"] as MutableList<String>
//                    )
////                    if(item.time!! < compare.time!!) continue
//                    if(item.start?.sggNm != compare?.start?.sggNm) continue
//                    if(item.end?.sggNm != compare?.end?.sggNm) continue
//
//                    Log.d(TAG,"filter passed")
//                    teamList.add(item)
//                }
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "error getting documents: $exception", exception)
//            }
//        teamList.add(Team(""))
    }

//    private fun filteringTeam(team: Team): Boolean{
//        if(team.curr!! < team.max!!) return false
//        return true
//    }
//    private fun filteringTeam(team: Team, pair: Pair<String,Any>): Boolean {
//        if(pair.first == "start") {
//            if (pair.second == team.start) { return false } // perfect matching
//        }
//        else if(pair.first == "end") {
//            if (pair.second == team.end) { return false }   // perfect matching
//            else {
//                // get coord of team.end and viewmodel.getEnd()
//                val lat1:Double=1.1
//                val lon1:Double=1.2
//                val lat2:Double=1.3
//                val lon2:Double=1.4
//                val dist:Double=1000.0  // ~1km
//                if(getDistance(lat1,lon1,lat2,lon2)<dist) { return false }
//            }
//        }
//        return true
//    }
//    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val dLat = Math.toRadians(lat2 - lat1)
//        val dLon = Math.toRadians(lon2 - lon1)
//        val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return CurrentInfo.EARTH_RADIUS * c * 1000
//    }
//
//    override fun onAdapterItemClickListener(position: Int) {
//        if(teamList[position].teamId != "") {
//            Log.d(CurrentInfo.TAG, "join team: Not Yet Implemented")
//            callFragment(0)  // go to create view
//        }
//        else{
//            viewmodel.setSearchingTeamOf(teamList[position].teamId!!)
//            callFragment(1)  // go to detail view
//        }
//    }
//
//    private fun callFragment(flag:Int){
//        if(flag==1) (activity as Act02Search).openSearchFragment()
//        else (activity as Act02Search).openSearchFragment()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mbinding = null
//    }
}
//
//class ListFragment : Fragment(), OnAdapterItemClickListener{
//    private var mbinding:ListFragmentBinding? = null
//    private val binding get() = mbinding!!
//    private val teamList = arrayListOf<Team>()
//    private lateinit var adapter:ListAdapter
//    private lateinit var compare:Team
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
//
//    override fun onAdapterItemClickListener(position: Int) {
//        if(teamList[position].teamId != "")
//            Log.d(TAG,"join team: Not Yet Implemented")
//        else{
//            val bundle = Bundle()
//            onSaveInstanceState(bundle)
//            bundle.putInt("dest",0)
//            bundle.putString("flow","create")
//            Log.d(TAG,"listFrag:bundle:${bundle.toString()}")
//            callFragment(bundle)
//        }
//    }
//
//    fun callFragment(bundle:Bundle){
//        (activity as Act02Search).replaceFragment(bundle)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putAll(arguments)
//        super.onSaveInstanceState(outState)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mbinding = null
//    }
//}