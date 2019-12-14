package com.example.chatonme.views.extra


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentChatBinding
import com.example.chatonme.databinding.FragmentFriendProfileBinding
import com.example.chatonme.models.User


class FriendProfileFragment : Fragment() {

    private lateinit var selectedUser: User
    private lateinit var binding: FragmentFriendProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendProfileBinding.inflate(inflater)

        //init variable
        selectedUser = arguments!!.getParcelable("selectedUser")!!

        return binding.root
    }


}
