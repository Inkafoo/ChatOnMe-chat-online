package com.example.chatonme


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


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

        //get user
        val user = FirebaseAuth.getInstance().currentUser
        when{
            user != null -> navigateToHomeFragment()
        }


        return binding.root
    }


    /**
     * Go to target (login/register fragment)
     */
    private fun navigate(view: View, target: Int){
        RxView.clicks(view).map{
            when (findNavController().currentDestination!!.id) {
                R.id.mainFragment -> findNavController().navigate(target)
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }


    /**
     * Navigates to homeFragment
     */
    private fun navigateToHomeFragment(){
        when (findNavController().currentDestination!!.id) {
            R.id.mainFragment -> findNavController().navigate(R.id.action_mainFragment_to_connectBottomNavigationFragment)
        }
    }

}
