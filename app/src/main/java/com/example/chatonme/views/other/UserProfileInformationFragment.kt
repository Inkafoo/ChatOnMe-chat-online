package com.example.chatonme.views.other


import android.os.Bundle
import android.transition.Visibility
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.databinding.FragmentUserProfileInformationBinding
import kotlinx.android.synthetic.main.fragment_connect_bottom_navigation.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfileInformationFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileInformationBinding.inflate(inflater)



        return binding.root
    }



}
