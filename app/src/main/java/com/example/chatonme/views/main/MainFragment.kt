package com.example.chatonme.views.main


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)

        navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment

        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )

        handleNavControllerDestination()

        return binding.root
    }

    /**
     * Changes toolbar title, sets menu and handle visibility of bottomNav and toolbar
     */
    private fun handleNavControllerDestination() {
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            binding.homeToolbar.title = getString(R.string.app_name) + " " + destination.label!!
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
                    R.id.addPostFragment
                        -> {
                            binding.homeToolbar.title = getString(R.string.create_post)
                            binding.homeToolbar.inflateMenu(R.menu.add_post_toolbar_menu)
                            binding.bottomNavigationView.visibility = View.GONE
                        }
                    else
                        -> {
                            binding.bottomNavigationView.visibility = View.VISIBLE
                            binding.homeToolbar.visibility = View.VISIBLE
                        }
            }
        }

    }

    /**
     * Sets bottomNavView as NavController
     */
    private fun setNavController() {
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentHost)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavController()
    }

}
