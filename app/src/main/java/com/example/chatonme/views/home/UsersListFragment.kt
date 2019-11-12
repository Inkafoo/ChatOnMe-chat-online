package com.example.chatonme.views.home


import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatonme.adapters.FriendsList
import com.example.chatonme.models.UsersListViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.databinding.FragmentUsersListBinding
import com.example.chatonme.models.User
import com.example.chatonme.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_users_list.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class UsersListFragment : Fragment() {

    private val usersListViewModel: UsersListViewModel by inject()
    private val userListAdapter: UserListAdapter by inject()
    private lateinit var binding: FragmentUsersListBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val friendadapater: FriendsList by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersListBinding.inflate(inflater)

        binding.recyclerUserList.adapter = userListAdapter
        binding.recyclerUserList.layoutManager = LinearLayoutManager(this.context)

        usersListViewModel.getRegisteredUsers()
        usersListViewModel.userLists.observe(this, Observer { users ->
            users?.let {
                userListAdapter.setUsers(it)
            }
        })

        binding.searchFriendEditText.setOnClickListener {
            searchFriend()
        }


        return binding.root
    }


    private fun searchFriend(){
        var list = arrayListOf<User>()
        searchFriendEditText.addTextChangedListener {
            val text = searchFriendEditText.editableText.toString().toLowerCase()
            val queryRef = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").startAt(text).endAt(text+"\uf8ff")

            queryRef.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(dataSnapShot: DatabaseError) {

                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    for(dataSnapShot in dataSnapShot.children){
                        val user = dataSnapShot.getValue(User::class.java)
                        list.add(user!!)
                    }

                    binding.recyclerUserList.adapter = friendadapater
                    binding.recyclerUserList.layoutManager = LinearLayoutManager(context)
                    friendadapater.setUsers(list)


                }

            })
        }
    }










}
