package com.example.chatonme.views.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.activity_basic.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment?

        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.userProfileInformationFragment ,
                R.id.chatFragment,
                R.id.friendProfileFragment,
                R.id.addPostFragment
                     -> {
                             binding.bottomNavigationView.visibility = View.GONE
                             //binding.homeToolbar.visibility = View.GONE
                     }

                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(
                binding.bottomNavigationView,
                navHostFragment.navController
            )
        }

       navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
           binding.homeToolbar.title = getString(R.string.app_name) + " " + destination.label!!
       }



        return binding.root
    }

    /**
     * Set bottomNavView as NavController
     */
    private fun setNavController(){
        val navController = Navigation.findNavController(requireActivity(),
            R.id.fragmentHost)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavController()
    }

}
