package com.example.chatonme.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.di.components.Messaging
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject

class PostsListViewModel(private val messaging: Messaging) : ViewModel() {
    private val mutableLiveDataList = MutableLiveData<MutableList<Post>>()
    private val postList = mutableListOf<Post>()

    /**
     * Gets post list from database
     */
    private fun getPostList(databaseReference: CollectionReference) : LiveData<MutableList<Post>> {
        databaseReference.get()
            .addOnSuccessListener {
                for (document in it) {
                    val post: Post = document.toObject()

                    postList.add(post)
                }
                mutableLiveDataList.value = postList
            }
            .addOnFailureListener {
                messaging.showToast("error", it.message.toString())
            }

        return mutableLiveDataList
    }

    fun showPostList(databaseReference: CollectionReference): LiveData<MutableList<Post>> {
        val mutableData = MutableLiveData<MutableList<Post>>()
        getPostList(databaseReference).observeForever { postList ->
            mutableData.value = postList
        }

        return mutableData
    }

}






























