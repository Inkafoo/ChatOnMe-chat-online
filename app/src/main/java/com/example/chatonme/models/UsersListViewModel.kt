package com.example.chatonme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.di.components.Messaging
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject

class UsersListViewModel(private val messaging: Messaging) : ViewModel() {
    val userList = MutableLiveData<MutableList<User>>()

    /**
     * Gets registered users from database
     */
    fun getUserList(
        currentUserId: String,
        databaseReference: CollectionReference
    ) : LiveData<MutableList<User>> {
        val mutableLiveDataList = mutableListOf<User>()

        databaseReference.get()
            .addOnSuccessListener {
                for (document in it) {
                    val user: User = document.toObject()

                    if(user.uid != currentUserId) {
                        mutableLiveDataList.add(user)
                    }
                }
                userList.value = mutableLiveDataList
            }
            .addOnFailureListener {
                messaging.showToast("error", it.message.toString())
            }

        return userList
    }

}
