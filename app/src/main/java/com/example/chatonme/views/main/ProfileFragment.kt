package com.example.chatonme.views.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.IMAGES_PROFILE
import com.example.chatonme.helpers.PICK_IMAGE_REQUEST
import com.example.chatonme.helpers.USERS_REFERENCE
import com.example.chatonme.models.UserProfileViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jakewharton.rxbinding2.view.RxView
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val currentUser =  FirebaseAuth.getInstance().currentUser
    private val userProfileViewModel: UserProfileViewModel by inject()
    private val messaging: Messaging by  inject()
    private val customDialog: CustomDialog by  inject()
    private val imageProcessing: ImageProcessing by  inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater)

        signOutListener(binding.signOutButton)
        imageListener(binding.profileImage)
        editProfileListener(binding.editProfileButton)

        setUserData()


        return binding.root
    }


    /**
     * Gets user's data and set to fragment layout
     */
    private fun setUserData() {
        userProfileViewModel.getUserData(currentUser!!.uid).observe(this, Observer { user ->
            binding.apply {
                displayPresentationTv.text = user.presentation
                displayAgeTv.text = user.age
                displayCountryTv.text = user.country
                displayEmailTv.text = user.email
            }
            imageProcessing.setImage(user.image.toString(), binding.profileImage)
        })
    }

    /**
     * Navigates to EditUserInformation fragment
     */
    private fun editProfileListener(view: View) {
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_profileFragment_to_userProfileInformationFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Opens gallery to pick profile photo
     */
    private fun imageListener(view: View) {
        RxView.clicks(view).map {
           Intent().apply {
               type = "image/*"
               action = Intent.ACTION_GET_CONTENT
               startActivityForResult(Intent.createChooser(this, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
           }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Returns and set picked image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data?.data != null)
        {
            //imageProcessing.setImage(data.data.toString(), profileImage)
            uploadImage(data.data!!)
        }else{
            messaging.showToast("error", getString(R.string.something_went_wrong_try_again))
        }
    }

    /**
     * Uploads picked profile image to firebase
     */
    private fun uploadImage(uri: Uri) {
        val progressDialog = customDialog.progressDialog(this.context!!, "Updating")
        val firebaseStorageReference = Firebase.storage.reference
        val firebaseDatabase = Firebase.firestore

        val storageReference = firebaseStorageReference.child(IMAGES_PROFILE  + currentUser?.uid)
        val databaseReference = firebaseDatabase.collection(USERS_REFERENCE).document(currentUser!!.uid)

        progressDialog.show()

         storageReference.putFile(uri).addOnSuccessListener {
             val uriTask = it.storage.downloadUrl
             while (!uriTask.isSuccessful);
             val uri: Uri = uriTask.result!!
             val results = HashMap<String, Any>()
             results["image"] = uri.toString()
             if(uriTask.isSuccessful && uriTask.isComplete) {
                 databaseReference.update(results)
                     .addOnSuccessListener {
                         messaging.showToast("success", getString(R.string.image_added_successfully))
                         progressDialog.hide()
                     }
                     .addOnFailureListener{
                         progressDialog.hide()
                         messaging.showToast("error", getString(R.string.something_went_wrong_try_again))
                     }
             }
        }.addOnFailureListener{
             progressDialog.hide()
             messaging.showToast("error",  getString(R.string.something_went_wrong_try_again))
         }
    }

    /**
     *Sign out and close the app
     */
    private fun signOutListener(view: View){
        RxView.clicks(view).map{
            AuthUI.getInstance().signOut(this.activity!!).addOnSuccessListener {
                context!!.cacheDir.deleteRecursively()
                activity!!.finishAffinity()
            }.addOnFailureListener {
                messaging.showSnackBar(binding.root, getString(R.string.something_went_wrong_try_again))
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }
}
