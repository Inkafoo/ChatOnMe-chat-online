package com.example.chatonme.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.helpers.POSTS_REFERENCE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostsListViewModel : ViewModel() {


    private fun getPosts() : LiveData<MutableList<Post>> {
        val data = MutableLiveData<MutableList<Post>>()
        val myList = mutableListOf<Post>()

        FirebaseDatabase.getInstance().getReference(POSTS_REFERENCE).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e(this.toString(), error.message )
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapShot in p0.children) {
                    val post = dataSnapShot.getValue(Post::class.java)

                    if (post != null) {
                        myList.add(post)
                    }

                }

                data.value = myList
            }
        })

        return data
        }


    fun fetchEventData():LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        getPosts().observeForever { eventList ->
            mutableData.value = eventList
        }

        return mutableData
    }

}

