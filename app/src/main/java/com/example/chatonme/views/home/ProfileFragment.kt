package com.example.chatonme.views.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.databinding.FragmentProfileBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater)

        signOutListener(binding.signOutButton)

        return binding.root
    }

    /**
     *Sign out and close the app
     */
    private fun signOutListener(view: View){
        RxView.clicks(view).map{
            AuthUI.getInstance().signOut(this!!.activity!!).addOnCompleteListener {
               activity!!.finish()
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

}
