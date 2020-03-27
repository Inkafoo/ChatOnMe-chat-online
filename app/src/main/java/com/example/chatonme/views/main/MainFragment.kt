package com.example.chatonme.views.main


import android.app.ActionBar
import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentMainBinding
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.Validators
import com.example.chatonme.views.extra.UserProfileInformationFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_user_profile_information.*
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject


class MainFragment : Fragment() {

    private val messaging: Messaging by inject()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment?

        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id != R.id.chatFragment){
                binding.homeToolbar.title = getString(R.string.app_name) + " " + destination.label!!
            }
        }

        navHostFragment?.navController?.addOnDestinationChangedListener { _, destination, _ ->
            binding.homeToolbar.menu.clear()

            when(destination.id) {
                R.id.userProfileInformationFragment
                    -> {
                         binding.bottomNavigationView.visibility = View.GONE
                         binding.homeToolbar.title = getString(R.string.profile_information)
                         binding.homeToolbar.inflateMenu(R.menu.user_profile_toolbar_menu)
                    }
                R.id.chatFragment
                    -> {
                         binding.bottomNavigationView.visibility = View.GONE
                         binding.homeToolbar.visibility = View.GONE
                    }
                else
                    -> {
                         binding.bottomNavigationView.visibility = View.VISIBLE
                         binding.homeToolbar.visibility = View.VISIBLE

                    }
            }
        }

        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(
                binding.bottomNavigationView,
                navHostFragment.navController
            )
        }


        return binding.root
    }


    /**
     * Sets bottomNavView as NavController
     */
    private fun setNavController(){
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentHost)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavController()
    }

}
