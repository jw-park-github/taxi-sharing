package com.example.prj1114.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prj1114.data.Chat
import com.example.prj1114.databinding.ChatFragmentBinding
import com.example.prj1114.viewmodel.DetailViewModel
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class ChatFragment: Fragment(){
    private var mBinding: ChatFragmentBinding? = null
    private val binding get() = mBinding!!
    private lateinit var adapter: ChatAdapter

    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = ChatFragmentBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.chatRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter = ChatAdapter(viewModel.getUserInfo(),viewModel.getChatList())
        binding.chatRecyclerview.adapter = adapter

        binding.editText.addTextChangedListener { text ->
            binding.btnSend.isEnabled = text.toString() != ""
        }

        binding.btnSend.setOnClickListener{
            lifecycleScope.launch {
                if (viewModel.sendChat(binding.editText.text.toString())) {
                    binding.editText.text.clear()
                }
                else {
                    Toast.makeText(context, "send failed",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getChat()
            binding.chatRecyclerview.scrollToPosition(viewModel.getChatList().size-1)
            viewModel.listenChat()

            viewModel.register.observe(viewLifecycleOwner, Observer {
                for (doc in it.documentChanges){
                    viewModel.addChatList(doc.document.toObject<Chat>())
                    binding.chatRecyclerview.scrollToPosition(viewModel.getChatList().size-1)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}

//class ChatFragment: Fragment(){
//    private var mBinding: ChatFragmentBinding? = null
//    private val binding get() = mBinding!!
//    private lateinit var currentUser:String
//    private lateinit var currentTeam:String
//    private lateinit var registration: ListenerRegistration
//    private val chatList = arrayListOf<Chat>()
//    private lateinit var adapter: ChatAdapter
//    private val db = Firebase.firestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        currentUser = CurrentInfo.userId
//        currentTeam = CurrentInfo.teamId
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mBinding = ChatFragmentBinding.inflate(inflater, container, false)
//        val view = binding.root
//        Log.d(CurrentInfo.TAG,"currentUser:${currentUser}")
//
//        binding.chatRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        adapter = ChatAdapter(currentUser,chatList)
//        binding.chatRecyclerview.adapter = adapter
//
//        binding.editText.addTextChangedListener { text ->
//            binding.btnSend.isEnabled = text.toString() != ""
//        }
//
//        binding.btnSend.setOnClickListener{
//            val data = Chat(currentUser, currentTeam, binding.editText.text.toString(), Timestamp.now())
//
//            db.collection(currentTeam).add(data)
//                .addOnSuccessListener {
//                    binding.editText.text.clear()
//                    Log.d(CurrentInfo.TAG, "document added: $it")
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(context, "send failed",Toast.LENGTH_SHORT).show()
//                    Log.w(CurrentInfo.TAG,"error occurs: $e",e)
//                }
//        }
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        db.collection(currentTeam)
//            .orderBy("time", Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    chatList.add(document.toObject<Chat>())
//                }
//                chatList.sortBy { it.time }
//                binding.chatRecyclerview.scrollToPosition(chatList.size-1)
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.w(CurrentInfo.TAG, "error getting documents: $exception", exception)
//            }
//        if (chatList.isEmpty()) {
//            Log.d(CurrentInfo.TAG,"empty chat?")
//            chatList.add(Chat(currentUser,currentTeam,"$currentUser entered", Timestamp.now()))
//        }
//        registration = db.collection(currentTeam)
//            .orderBy("time",Query.Direction.DESCENDING)
//            .addSnapshotListener { snapshots, e ->
//                if(e!=null){
//                    Log.w(CurrentInfo.TAG,"listen failed: $e",e)
//                    return@addSnapshotListener
//                }
//                if(snapshots!!.metadata.isFromCache) return@addSnapshotListener
//                for(doc in snapshots.documentChanges){
//                    Log.d(CurrentInfo.TAG,doc.toString())
//                    chatList.add(doc.document.toObject<Chat>())
//                    adapter.notifyDataSetChanged()
//                    binding.chatRecyclerview.scrollToPosition(chatList.size-1)
//                }
//            }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        registration.remove()
//        mBinding = null
//    }
//}