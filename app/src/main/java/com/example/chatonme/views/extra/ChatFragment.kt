package com.example.chatonme.views.extra

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.R
import com.example.chatonme.adapters.MessageFromAdapter
import com.example.chatonme.adapters.MessageToAdapter
import com.example.chatonme.databinding.FragmentChatBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.FormatDate
import com.example.chatonme.models.ChatMessage
import com.example.chatonme.models.User
import com.google.android.gms.common.util.DataUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class ChatFragment : Fragment() {

    private val messaging: Messaging by inject()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser =  FirebaseAuth.getInstance().currentUser!!
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var currentUserData: User
    private lateinit var selectedUser: User
    private lateinit var fromReferenceDatabase: DatabaseReference
    private lateinit var toReferenceDatabase: DatabaseReference
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and set adapter for recyclerView
        binding = FragmentChatBinding.inflate(inflater)
        binding.chatRecyclerView.adapter = adapter

        //init variable
        selectedUser = arguments!!.getParcelable("selectedUser")!!
        fromReferenceDatabase = firebaseDatabase.getReference("user-messages/${currentUser.uid}/${selectedUser.uid}")
        toReferenceDatabase = firebaseDatabase.getReference("user-messages/${selectedUser.uid}/${currentUser.uid}")


        getCurrentUserData()
        listenerForMessages()

        sendMessageListener(binding.sendMessageButton)

        return binding.root
    }

    /**
     * Get data about current user from firebase
     */
    private fun getCurrentUserData(){
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    currentUserData  = item.getValue(User::class.java)!!
                }
            }
            override fun onCancelled(error: DatabaseError) {
                messaging.showToast("error", error.message)
            }
        })

    }


    /**
     * Listen for messages and show in recyclerView
     */
    private fun listenerForMessages(){
        fromReferenceDatabase.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val chatMessage = dataSnapshot.getValue(ChatMessage::class.java)

                if(chatMessage!!.fromId == currentUser.uid){
                    adapter.add(MessageToAdapter(
                        chatMessage,
                        currentUserData.image.toString(),
                        imageProcessing = ImageProcessing(activity!!.applicationContext)))
                }else{
                    adapter.add(MessageFromAdapter(
                        chatMessage,
                        selectedUser.image.toString(),
                        imageProcessing = ImageProcessing(activity!!.applicationContext)))
                }

                chatRecyclerView.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }


    /**
     *  Send message button listener
     */
    private fun sendMessageListener(view: View){
        RxView.clicks(view).map {
            val currentTime = System.currentTimeMillis() / 1000
            val textMessage = binding.messageEditText.text.toString()

            if(textMessage.isEmpty()){
                messaging.showToast("error",  getString(R.string.message_cannot_be_empty))
            }
            else
            {
                val chatMessage = ChatMessage(
                    toReferenceDatabase.key!!,
                    textMessage, currentUser.uid,
                    selectedUser.uid.toString(),
                    currentTime
                )

                fromReferenceDatabase.push().setValue(chatMessage)
                    .addOnSuccessListener {
                        binding.messageEditText.text.clear()
                        chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    }.addOnFailureListener {
                        messaging.showToast("error", it.message.toString())
                    }

                toReferenceDatabase.push().setValue(chatMessage)
                    .addOnSuccessListener {
                        binding.messageEditText.text.clear()
                    }.addOnFailureListener {
                        messaging.showToast("error", it.message.toString())
                    }
            }

        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

}



























