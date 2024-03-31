package com.example.prj1114.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.prj1114.common.CurrentInfo
import com.example.prj1114.data.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.Current
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class DetailViewModel : ViewModel() {
    private val repository = Repository()

    /** 데이터 대기중을 나타내는 liveData */
    private val _isAwait = MutableLiveData(true)
    val isAwait: LiveData<Boolean> get() = _isAwait

    /** DetailFragment 화면에 뿌려줄 데이터 */
    private val sampleTeamId = CurrentInfo.teamId
    lateinit var sampleTeam: Team
    lateinit var max: String
    lateinit var time: String
    lateinit var start: String
    lateinit var end: String

    var joinTime:Long = 0
    private val _btnText = MutableLiveData<Boolean>()
    val btnText: LiveData<Boolean> get() = _btnText

    private fun setAwait(prop: Boolean) {
        _isAwait.value = prop
    }
    private fun initForTest(){
        time = "aa"
        max = "bb"
        start ="cc"
        end ="dd"
    }

    suspend fun getTargetTeam() {
        Log.d("APPLE", "coroutine launch")
        initForTest()
        repository.getTeam(sampleTeamId)?.let {
            Log.d("APPLE", "$it")
            time = convertLongToDate(it.time!!)
            max = convertMaxString(it.max, it.curr)
            start = it.start!!.emdNm
            end = it.end!!.emdNm
        }
        repository.getUserTeam(CurrentInfo.userId,sampleTeamId)?.let {
            joinTime = it.joinTime!!
        }
        setAwait(false)
    }

    suspend fun joinTargetTeam() {
        Log.d(CurrentInfo.TAG,"DVM: joinTargetTeam")
        repository.createUserTeam(CurrentInfo.userId,sampleTeamId)?.let {
            if(it!="MAX ERROR") {
                Log.d(CurrentInfo.TAG,"DVM: joined??")
                joinTime = Calendar.getInstance().timeInMillis
            }
        }
    }

    fun setButton(){
        Log.d(CurrentInfo.TAG,"DVM: setButton")
        _btnText.value = joinTime>0
    }

    private fun convertMaxString(max: Int?, curr: Int?): String {
        return "$curr / $max"
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("MM.dd HH:mm", Locale.KOREA)
        return format.format(date)
    }

    private val chatList = arrayListOf<Chat>()
    lateinit var register:LiveData<QuerySnapshot>

    fun getUserInfo():String{ return CurrentInfo.userId }

    fun getChatList():List<Chat>{ return chatList }
    fun addChatList(chat:Chat){ chatList.add(chat) }

    suspend fun sendChat(message: String): Boolean{
        return !Repository().createChat(Chat(
            userId = CurrentInfo.userId,
            teamId = CurrentInfo.teamId,
            message = message,
            time = Timestamp.now().seconds)).isNullOrEmpty()
    }
    suspend fun getChat(){
        Repository().getAllTeamChat(CurrentInfo.teamId,joinTime)
            ?.let { chatList.addAll(it) }
        if(chatList.isEmpty()) {
            chatList.add(Chat(
                userId = CurrentInfo.userId,
                teamId = CurrentInfo.teamId,
                message = "${CurrentInfo.userId} entered",
                time = Timestamp.now().seconds))
        }
    }
    suspend fun listenChat(){
        register = Repository().listenAllTeamChat(CurrentInfo.teamId).asLiveData()
    }
}

//    private val naver = NaverLogin()
//    private val job = viewModelScope
//    private val repository2 = ChatRepository()
//    private lateinit var registration: ListenerRegistration
//
//    lateinit var currentUser: String
//    lateinit var currentTeam: String
//    init {
//        job.launch {
////            currentUser = naver.getCurrentUser()
//            currentUser = CurrentInfo.userId
//            currentTeam = CurrentInfo.teamId
//        }
//    }
//
//    /** liveData */
//    private var _input = MutableLiveData<String>()
//    val input: LiveData<String> get() = _input
//
//    val textLiveData = MutableLiveData<String>()
//    val chatLiveData = MutableLiveData<List<Chat>>()
//    private var textData = String()
//    private val chatData = arrayListOf<Chat>()
//
//    fun setInput(text: String) {
//        textData = text
//    }
//
//    /** save to repository */
//    fun sendChat():Boolean {
//        val data = Chat(currentUser,currentTeam,textData, Timestamp.now())
//        Log.d(CurrentInfo.TAG,"DVM:sendChat $data")
//        return repository2.add(data)
//    }
//    fun inChat(): Chat {
//        return Chat(currentUser,currentTeam,textData, Timestamp.now())
//    }
//    fun outChat(documents: QuerySnapshot){
//        for(doc in documents){
//            chatData.add(doc.toObject<Chat>())
//        }
//        chatData.sortBy { it.time }
//    }
//    fun outChat2(documents: QuerySnapshot){
//        for(doc in documents.documentChanges){
//            chatData.add(doc.document.toObject<Chat>())
//        }
//        chatData.sortBy { it.time }
//    }
//    fun checkEmpty(){
//        if(chatData.isEmpty()) {
//            chatData.add(
//                Chat(
//                    currentUser, currentTeam, "entered room",
//                    Timestamp.now()
//                )
//            )
//        }
//    }
//    /** get Chat data from db */
//    fun getChat() {
//        Log.d(CurrentInfo.TAG,"DVM:getChat run")
//        if(chatData.isEmpty()){
//            Log.d(CurrentInfo.TAG,"DVM:getChat: isEmpty")
//            repository2.get("Chat",Pair("teamId",currentTeam),Pair("time",CurrentInfo.joinDate))
//                .get()
//                .addOnSuccessListener {
//                    Log.d(CurrentInfo.TAG,"DVM:getChat:success $it")
//                    for(doc in it){
//                        Log.d(CurrentInfo.TAG,doc.toString())
//                        chatData.add(doc.toObject<Chat>())
//                    }
//                }
//                .addOnFailureListener {
//                    Log.d(CurrentInfo.TAG,"DVM:getChat:fail")
//                }
//            chatData.sortBy { it.time }
//        }
//        if(chatData.isEmpty()){
//            chatData.add(Chat(currentUser,currentTeam,"entered room",
//                Timestamp.now()))
//        }
//        Log.d(CurrentInfo.TAG,"chatdata: $chatData")
//        syncData()
//    }
//    fun listenChat(){
//        Log.d(CurrentInfo.TAG,"DVM:listenChat run")
//        registration = repository2.get(currentTeam, Pair("teamId",currentTeam))
//            .addSnapshotListener { snapshots,e ->
//                Log.d(CurrentInfo.TAG,"DVM:listener run $snapshots")
//                if(e!=null) return@addSnapshotListener
//                if(snapshots!!.metadata.isFromCache) return@addSnapshotListener
//                for(doc in snapshots.documentChanges){
//                    Log.d(CurrentInfo.TAG,doc.document.toString())
//                    chatData.add(doc.document.toObject<ChatDto>().asDomain())
//                    syncData()
//                }
//            }
//    }
//    fun syncData(){
//        Log.d(CurrentInfo.TAG,"DVM:syncData")
//        chatLiveData.value = chatData
//    }
//    fun removeRegis(){
//        registration.remove()
//    }
//}