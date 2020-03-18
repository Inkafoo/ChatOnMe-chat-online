package com.example.chatonme.views.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.chatonme.adapters.FriendsList
import com.example.chatonme.models.UsersListViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.databinding.FragmentUsersListBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class UsersListFragment : Fragment() {

    private val usersListViewModel: UsersListViewModel by inject()
    private val userListAdapter: UserListAdapter by inject()
    private val friendadapater: FriendsList by inject()
    private val currentUser= FirebaseAuth.getInstance().currentUser
    private lateinit var binding: FragmentUsersListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersListBinding.inflate(inflater)

        binding.recyclerUserList.adapter = userListAdapter

        usersListViewModel.getRegisteredUsers(currentUser!!.uid)
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
       //val list = arrayListOf<User>()
       //searchFriendEditText.addTextChangedListener {
       //    val text = searchFriendEditText.editableText.toString().toLowerCase()
       //    val queryRef = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name").startAt(text).endAt(text+"\uf8ff")

       //    queryRef.addValueEventListener(object : ValueEventListener{
       //        override fun onCancelled(dataSnapShot: DatabaseError) {

       //        }

       //        override fun onDataChange(dataSnapShot: DataSnapshot) {
       //            for(dataSnapShot in dataSnapShot.children){
       //                val user = dataSnapShot.getValue(User::class.java)
       //                list.add(user!!)
       //            }

       //            binding.recyclerUserList.adapter = friendadapater
       //            binding.recyclerUserList.layoutManager = LinearLayoutManager(context)
       //            friendadapater.setUsers(list)


       //        }

       //    })
       //}
    }










}
