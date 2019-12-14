package com.example.chatonme.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.helpers.USERS_REFERENCE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersListViewModel : ViewModel() {
    val userLists: MutableLiveData<List<Users>> = MutableLiveData()

    /**
     * get registered users list
     */
    fun getRegisteredUsers(currentUserId: String){
        FirebaseDatabase.getInstance().getReference(USERS_REFERENCE).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e(this.toString(), error.message )
            }

            override fun onDataChange(p0: DataSnapshot) {
                val users = mutableListOf<Users>()
                for (dataSnapShot in p0.children) {
                    val user = dataSnapShot.getValue(Users::class.java)

                    if(currentUserId != user!!.uid){
                        users.add(user)
                    }
                }
                userLists.postValue(users)
            }
        })
    }

}
