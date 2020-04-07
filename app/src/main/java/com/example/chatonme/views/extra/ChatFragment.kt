package com.example.chatonme.views.extra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatonme.R
import com.example.chatonme.adapters.MessageFromAdapter
import com.example.chatonme.adapters.MessageToAdapter
import com.example.chatonme.databinding.FragmentChatBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.POSTS_REFERENCE
import com.example.chatonme.helpers.USERS_REFERENCE
import com.example.chatonme.helpers.USER_MESSAGES
import com.example.chatonme.models.ChatMessage
import com.example.chatonme.models.User
import com.example.chatonme.views.start.BasicActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.view.RxView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {

    private lateinit var currentUserData: User
    private lateinit var selectedUser: User
    private lateinit var fromReferenceDatabase: CollectionReference
    private lateinit var toReferenceDatabase: CollectionReference
    private lateinit var binding: FragmentChatBinding
    private val database = Firebase.firestore
    private val usersReference = Firebase.firestore.collection(USERS_REFERENCE)
    private val currentUser =  FirebaseAuth.getInstance().currentUser!!
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val messaging: Messaging by inject()
    private val imageProcessing: ImageProcessing by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and set adapter of recyclerView
        binding = FragmentChatBinding.inflate(inflater)
        binding.chatRecyclerView.adapter = adapter

        //init variable
        selectedUser = arguments!!.getParcelable("selectedUser")!!

        fromReferenceDatabase = database.collection(USER_MESSAGES + "${currentUser.uid}/${selectedUser.uid}")
        toReferenceDatabase = database.collection(USER_MESSAGES + "${selectedUser.uid}/${currentUser.uid}")


        setUpToolbarData()
        getCurrentUserData()
        listenerForMessages()
        sendMessageListener(binding.sendMessageButton)

        return binding.root
    }



    /**
     * Sets chat partner's name, age and image
     */
    private fun setUpToolbarData() {
        binding.pickedUserNameTextView.text = "${selectedUser.name}"
        binding.pickedUserAgeTextView.text = "${selectedUser.age}"

        imageProcessing.setImage(selectedUser.image.toString(), binding.itemImageView)
    }

    /**
     * Gets data about current user from firebase
     */
    private fun getCurrentUserData() {
        usersReference.get()
            .addOnSuccessListener {
                for(item in it) {
                    currentUserData = item.toObject()
                }
            }
            .addOnFailureListener {
                messaging.showToast("error", it.message.toString())
            }
    }

    /**
     * Listens for messages and show in recyclerView
     */
    private fun listenerForMessages() {
        fromReferenceDatabase.document(selectedUser.uid.toString()).addSnapshotListener { snapshot, e ->
            val chatMessage = snapshot?.toObject(ChatMessage::class.java)

            if(snapshot != null && snapshot.exists()){
                if (chatMessage!!.fromId == currentUser.uid) {
                    adapter.add(
                        MessageToAdapter(
                            chatMessage,
                            currentUserData.image.toString(),
                            imageProcessing = ImageProcessing(activity!!.applicationContext)
                        )
                    )
                } else {
                    adapter.add(
                        MessageFromAdapter(
                            chatMessage,
                            selectedUser.image!!,
                            imageProcessing = ImageProcessing(activity!!.applicationContext)
                        )
                    )
                }
                chatRecyclerView.scrollToPosition(adapter.itemCount - 1)

            } else {
                if (e != null) {
                    messaging.showToast("error", e.message.toString())
                } else {
                    messaging.showToast("error", "error")
                }
            }
        }
    }


    /**
     *  Sends message button listener
     */
    private fun sendMessageListener(view: View) {
        RxView.clicks(view).map {
            val currentTime = System.currentTimeMillis() / 1000
            val textMessage = binding.messageEditText.text.toString()

            if(textMessage.isEmpty()){
                messaging.showToast("error",  getString(R.string.message_cannot_be_empty))
            }
            else
            {
                val chatMessage = ChatMessage(
                        textMessage,
                        currentUser.uid,
                        selectedUser.uid.toString(),
                        currentTime
                    )

                toReferenceDatabase.add(chatMessage)
                    .addOnSuccessListener {
                        binding.messageEditText.text.clear()
                        chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
                    }.addOnFailureListener {
                        messaging.showToast("error", it.message.toString())
                    }

                fromReferenceDatabase.add(chatMessage)
                    .addOnSuccessListener {
                        binding.messageEditText.text.clear()
                    }.addOnFailureListener {
                        messaging.showToast("error", it.message.toString())
                    }
            }

        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Removes view  of fragment toolbar
     */
    override fun onDestroy() {
        super.onDestroy()
        (activity as BasicActivity).homeToolbar.removeView(view)
    }
}



























