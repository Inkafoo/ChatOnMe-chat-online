package com.example.chatonme.views.home


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.PICK_IMAGE_REQUEST
import com.firebase.ui.auth.AuthUI
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {

    private val messaging: Messaging by  inject()
    private val imageProcessing: ImageProcessing by  inject()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater)

        signOutListener(binding.signOutButton)
        imageListener(binding.profileImage)


        return binding.root
    }





    private fun imageListener(view: View){
        RxView.clicks(view).map {
           Intent().apply {
               type = "image/*"
               action = Intent.ACTION_GET_CONTENT
               startActivityForResult(Intent.createChooser(this, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
           }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data?.data != null
        ) {
            imageProcessing.setImageCenterFit(data.data!!, profileImage)
        }
    }






    /**
     *Sign out and close the app
     */
    private fun signOutListener(view: View){
        RxView.clicks(view).map{
            AuthUI.getInstance().signOut(this.activity!!).addOnSuccessListener {
                activity?.finish()
            }.addOnFailureListener {
                messaging.showSnackBar(binding.root, getString(R.string.something_went_wrong_try_again))
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }
}
