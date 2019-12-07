package com.example.chatonme.views.extra


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.R
import com.example.chatonme.adapters.MessageFromAdapter
import com.example.chatonme.adapters.MessageToAdapter
import com.example.chatonme.databinding.FragmentChatBinding
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.PICK_IMAGE_REQUEST
import com.example.chatonme.models.ChatMessage
import com.example.chatonme.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class ChatFragment : Fragment() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser =  FirebaseAuth.getInstance().currentUser!!
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val messaging: Messaging by inject()
    private lateinit var selectedUser: User
    private lateinit var fromReferenceDatabase: DatabaseReference
    private lateinit var toReferenceDatabase: DatabaseReference
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater)

        binding.chatRecyclerView.adapter = adapter
        selectedUser = arguments!!.getParcelable("selectedUser")!!
        fromReferenceDatabase = firebaseDatabase.getReference("user-messages/${currentUser.uid}/${selectedUser.uid.toString()}")
        toReferenceDatabase = firebaseDatabase.getReference("user-messages/${selectedUser.uid.toString()}/${currentUser.uid}")




        listenerForMessages()



        return binding.root
    }

    private fun listenerForMessages(){
        fromReferenceDatabase.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val chatMessage = dataSnapshot.getValue(ChatMessage::class.java)

                if(chatMessage!!.fromId == currentUser.uid){
                    adapter.add(MessageToAdapter(chatMessage.text))
                }else{
                    adapter.add(MessageFromAdapter(chatMessage.text))
                }

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
            val textMessage = binding.messageEditText.text.toString()
            val chatMessage = ChatMessage(
                    fromReferenceDatabase.key!!,
                    textMessage, currentUser.uid,
                    selectedUser.uid.toString()
            )

            fromReferenceDatabase.push().setValue(chatMessage)
                .addOnSuccessListener {
                    binding.messageEditText.text.clear()
                    chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }.addOnFailureListener {
                    messaging.showToast("error", "error")
                }

            toReferenceDatabase.setValue(chatMessage)
                .addOnSuccessListener {
                    binding.messageEditText.text.clear()
                }.addOnFailureListener {
                    messaging.showToast("error", "error")
                }

        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendMessageListener(binding.sendMessageButton)
    }
}



























