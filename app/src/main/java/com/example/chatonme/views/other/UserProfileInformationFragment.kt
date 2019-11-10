package com.example.chatonme.views.other


import android.os.Bundle
import android.text.InputType
import android.transition.Visibility
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.databinding.FragmentUserProfileInformationBinding
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_connect_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_user_profile_information.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class UserProfileInformationFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileInformationBinding
    private val customDialog: CustomDialog by inject()
    private val messaging: Messaging by inject()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceUsers = firebaseDatabase.getReference("Users")
    private val currentUser = FirebaseAuth.getInstance().currentUser

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

    private fun updateDataListener(view: View){
        RxView.clicks(view).map {
            val presentation = presentationEditText.text.toString()
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val country = countryEditText.text.toString()

            val user = User(
                name = name,
                presentation = presentation,
                age = age,
                country = country
            )

            firebaseDatabase.reference.child("Users").child(currentUser!!.uid).setValue(user.toMap())
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
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
        currentUser!!.updateEmail(email).addOnCompleteListener {
            messaging.showToast("success", getString(R.string.email_updated_successfully))
            referenceUsers.child(FirebaseAuth.getInstance().currentUser!!.uid).child("email").setValue(email)
        }.addOnFailureListener {
            messaging.showToast("error", getString(R.string.failed_to_update_email))
        }
    }
}
