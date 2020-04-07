package com.example.chatonme.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.USERS_REFERENCE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserProfileViewModel(private val messaging: Messaging) : ViewModel() {
    private val userList: MutableLiveData<User> = MutableLiveData()

    /**
     * Gets current user data from database
     */
    fun getUserData(currentUserId: String): MutableLiveData<User> {
        Firebase.firestore.collection(USERS_REFERENCE).get()
            .addOnSuccessListener {
                for(document in it) {
                    val modelUser: User = document.toObject()

                    if(modelUser.uid == currentUserId){
                        userList.value = modelUser
                    }
                }
            }
            .addOnFailureListener {
                messaging.showToast("error", it.message.toString())
            }

        return userList
    }

}