package com.example.chatonme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.di.components.Messaging
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject

class PostsListViewModel(private val messaging: Messaging) : ViewModel() {
    val postList = MutableLiveData<MutableList<Post>>()

    /**
     * Gets post list from database
     */
    fun getPostList(databaseReference: CollectionReference) : LiveData<MutableList<Post>> {
        val mutableLiveDataList = mutableListOf<Post>()
        databaseReference.get()
            .addOnSuccessListener {
                for (document in it) {
                    val post: Post = document.toObject()

                    mutableLiveDataList.add(post)
                }
                postList.value = mutableLiveDataList
            }
            .addOnFailureListener {
                messaging.showToast("error", it.message.toString())
            }

        return postList
    }

}






























