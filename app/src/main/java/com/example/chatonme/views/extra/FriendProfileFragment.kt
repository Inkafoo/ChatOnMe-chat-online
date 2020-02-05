package com.example.chatonme.views.extra


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.chatonme.databinding.FragmentFriendProfileBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.models.User
import com.example.chatonme.models.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class FriendProfileFragment : Fragment() {

    private val userProfileViewModel: UserProfileViewModel by inject()
    private val imageProcessing: ImageProcessing by  inject()
    private lateinit var selectedUser: User
    private lateinit var binding: FragmentFriendProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendProfileBinding.inflate(inflater)

        //init variable
        selectedUser = arguments!!.getParcelable("selectedUser")!!

        userProfileViewModel.getUserData(selectedUser.uid!!).observe(this, Observer { user ->
            binding.apply {
                displayPresentationTv.text = user.presentation
                displayAgeTv.text = user.age
                displayCountryTv.text = user.country
                displayEmailTv.text = user.email
            }
            imageProcessing.setImage(user.image.toString(), binding.profileImage)
        })

        return binding.root
    }


}
