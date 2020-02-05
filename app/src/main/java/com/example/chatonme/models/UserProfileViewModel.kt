package com.example.chatonme.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.USERS_REFERENCE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileViewModel(val messaging: Messaging) : ViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()

    /**
     * get current user data
     */
    fun getUserData(currentUserId: String): MutableLiveData<User> {
        FirebaseDatabase.getInstance().getReference(USERS_REFERENCE).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    val modelUser: User = item.getValue(User::class.java)!!

                    if(modelUser.uid == currentUserId){
                        user.value = modelUser
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                messaging.showToast("error", error.message)
            }
        })
        return user
    }

}