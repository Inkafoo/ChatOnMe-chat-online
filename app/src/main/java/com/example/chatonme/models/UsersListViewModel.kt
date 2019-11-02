package com.example.chatonme.models


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersListViewModel : ViewModel() {
    val userLists: MutableLiveData<List<Users>> = MutableLiveData()

    fun getUsers(firebaseDatabase: FirebaseDatabase){
        val referenceDatabase = firebaseDatabase.getReference("Users")
        referenceDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(this.toString(), error.message )
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = mutableListOf<Users>()
                for (dataSnapShot in p0.children) {
                    val users = dataSnapShot.getValue(Users::class.java)
                    user.add(users!!)
                }
                userLists.postValue(user)
            }
        })
    }
}
