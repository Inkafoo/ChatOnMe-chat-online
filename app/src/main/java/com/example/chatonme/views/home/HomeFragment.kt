package com.example.chatonme.views.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentHomeBinding
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        navigateToAddPostListener(binding.addPostFloatingButton)


        return binding.root
    }


    /**
     * Navigate to AddPostFragment
     */
    private fun navigateToAddPostListener(view: View){
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }
}
