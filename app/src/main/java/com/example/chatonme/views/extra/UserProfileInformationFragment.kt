package com.example.chatonme.views.extra


import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentUserProfileInformationBinding
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.USERS_REFERENCE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_user_profile_information.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class UserProfileInformationFragment : Fragment() {
    private val customDialog: CustomDialog by inject()
    private val messaging: Messaging by inject()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceUsers = firebaseDatabase.getReference(USERS_REFERENCE)
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var binding: FragmentUserProfileInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileInformationBinding.inflate(inflater)

        changeEmailListener(binding.changeEmailButton)
        updateDataListener(binding.updateDataButton)



        return binding.root
    }


    /**
     *get data entered by user
     */
    private fun updateDataListener(view: View){
        RxView.clicks(view).map {
            val progressBar = customDialog.progressDialog(this.context!!, getString(R.string.updating_information))
            val presentation = presentationEditText.text.toString()
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val country = countryEditText.text.toString()

            updateUserInformation(progressBar, presentation, name, age, country)

        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * update user information in firebase database
     */
    private fun updateUserInformation(
            progressBar: AlertDialog,
            presentation: String,
            name: String,
            age: String,
            country: String
    ){
        firebaseDatabase.reference.child(USERS_REFERENCE).child(currentUser!!.uid).apply {
            child("presentation").setValue(presentation)
            child("name").setValue(name)
            child("age").setValue(age)
            child("country").setValue(country)
                .addOnSuccessListener {
                    progressBar.cancel()
                    messaging.showToast("success", getString(R.string.information_updated_successfully))
                }.addOnFailureListener{
                    progressBar.cancel()
                    messaging.showToast("error", getString(R.string.something_went_wrong_try_again))
                }
        }
    }

    /**
     * show dialog to update email
     */
    private fun changeEmailListener(view: View){
        RxView.clicks(view).map {
            customDialog.materialDialog(this.context!!).show {
                title(text = getString(R.string.update_your_email))
                input(
                    hint = getString(R.string.new_email),
                    waitForPositiveButton = true,
                    allowEmpty = false,
                    inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                )
                positiveButton(text = getString(R.string.update)){
                    updateEmail(this.getInputField().text.toString())
                }
                negativeButton(text = getString(R.string.cancel)) {
                    this.dismiss()
                }
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * update email in Firebase database
     */
    private fun updateEmail(email: String){
        currentUser!!.updateEmail(email)
            .addOnCompleteListener {
                messaging.showToast("success", getString(R.string.email_updated_successfully))
                referenceUsers.child(FirebaseAuth.getInstance().currentUser!!.uid).child("email").setValue(email)
            }
            .addOnFailureListener {
                messaging.showToast("error", getString(R.string.failed_to_update_email))
            }
    }
}
