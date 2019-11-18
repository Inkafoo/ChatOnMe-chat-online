package com.example.chatonme.views.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentConnectBottomNavigationBinding
import kotlinx.android.synthetic.main.fragment_connect_bottom_navigation.*

class ConnectBottomNavigationFragment : Fragment() {

    private lateinit var binding: FragmentConnectBottomNavigationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConnectBottomNavigationBinding.inflate(inflater)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottomNavigationFragment) as NavHostFragment?
        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.userProfileInformationFragment ,
                R.id.chatFragment,
                R.id.friendProfileFragment
                     -> binding.bottomNavView.visibility = View.GONE

                else -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavController()
    }

    /**
     * Set bottomNavView as NavController
     */
    private fun setNavController(){
        val navController = Navigation.findNavController(requireActivity(),
            R.id.bottomNavigationFragment)
        bottomNavView.setupWithNavController(navController)
    }


}
