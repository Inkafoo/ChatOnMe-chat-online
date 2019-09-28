package com.example.chatonme


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatonme.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)

        navigate(binding.loginBtn, R.id.action_mainFragment_to_loginFragment)
        navigate(binding.registerBtn, R.id.action_mainFragment_to_registerFragment)


        return binding.root
    }

    private fun navigate(view: View, target: Int){
        view.setOnClickListener{
            when (findNavController().currentDestination!!.id) {
                R.id.mainFragment -> findNavController().navigate(target)
            }
        }
    }

}
