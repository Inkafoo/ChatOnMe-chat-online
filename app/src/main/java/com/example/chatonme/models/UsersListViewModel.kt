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
    private val mutableLiveDataList = MutableLiveData<List<User>>()
    private val userList = mutableListOf<User>()

    /**
     * Gets registered users list
     */
    fun getRegisteredUsers(currentUserId: String){
        FirebaseDatabase.getInstance().getReference(USERS_REFERENCE).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e(this.toString(), error.message )
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapShot in p0.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if(currentUserId != user!!.uid){
                        mutableLiveDataList.value = user
                    }
                }
                userLists.postValue(users)
            }
        })
    }

}
