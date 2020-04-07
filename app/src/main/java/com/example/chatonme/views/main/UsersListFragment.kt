package com.example.chatonme.views.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.chatonme.models.UsersListViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.databinding.FragmentUsersListBinding
import com.example.chatonme.helpers.USERS_REFERENCE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding
    private val databaseReference = Firebase.firestore.collection(USERS_REFERENCE)
    private val currentUser= FirebaseAuth.getInstance().currentUser
    private val usersListViewModel: UsersListViewModel by inject()
    private val userListAdapter: UserListAdapter by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersListBinding.inflate(inflater)

        binding.recyclerUserList.adapter = userListAdapter

        usersListViewModel.getUserList(currentUser!!.uid, databaseReference)
        usersListViewModel.userList.observe(this, Observer { users ->
            users?.let {
                userListAdapter.setUsers(it)
            }
        })


        return binding.root
    }

}










