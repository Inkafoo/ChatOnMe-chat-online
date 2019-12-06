package com.example.chatonme.views.extra


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.adapters.MessageFromAdapter
import com.example.chatonme.adapters.MessageToAdapter
import com.example.chatonme.databinding.FragmentChatBinding
import com.example.chatonme.models.User
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


class ChatFragment : Fragment() {

    private val currentUser =  FirebaseAuth.getInstance().currentUser
    private lateinit var selectedUser: User
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater)

        adapter.add(MessageFromAdapter())
        adapter.add(MessageFromAdapter())
        adapter.add(MessageToAdapter())
        adapter.add(MessageFromAdapter())
        adapter.add(MessageToAdapter())
        adapter.add(MessageToAdapter())

        binding.chatRecyclerView.adapter = adapter








        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectedUser = arguments!!.getParcelable("selectedUser")!!
        binding.messageEditText.hint = selectedUser?.name.toString()
    }

}
