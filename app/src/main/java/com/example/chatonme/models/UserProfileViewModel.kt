package com.example.chatonme.models


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileViewModel(val messaging: Messaging, val imageProcessing: ImageProcessing) : ViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()

    /**
     * Get and set user information
     */
    fun setUserData(binding: FragmentProfileBinding) {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    val modelUser: User = item.getValue(User::class.java)!!
                    binding.apply {
                        presentationTextView.text = modelUser.presentation
                        displayAgeTv.text = modelUser.age
                        displayCountryTv.text = modelUser.country
                        displayEmailTv.text = modelUser.email
                    }
                    imageProcessing.setImage(modelUser.image.toString(), binding.profileImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                messaging.showToast("error", error.message)
            }
        })
    }
}