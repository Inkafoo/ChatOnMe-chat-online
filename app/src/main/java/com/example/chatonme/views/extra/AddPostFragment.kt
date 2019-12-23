package com.example.chatonme.views.extra


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater)




        return binding.root
    }


}
