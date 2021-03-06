package com.example.chatonme.views.extra

import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentUserProfileInformationBinding
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.USERS_REFERENCE
import com.example.chatonme.helpers.Validators
import com.example.chatonme.models.UserProfileViewModel
import com.example.chatonme.views.start.BasicActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_user_profile_information.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class UserProfileInformationFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileInformationBinding
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val databaseReference = Firebase.firestore.collection(USERS_REFERENCE)
    private val userProfileViewModel: UserProfileViewModel by inject()
    private val customDialog: CustomDialog by inject()
    private val messaging: Messaging by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileInformationBinding.inflate(inflater)

        setToolbarMenu()
        displayUserData()
        changeEmailListener(binding.changeEmailButton)



        return binding.root
    }

    /**
     * Inflates menu and set to toolbar
     */
    private fun setToolbarMenu() {
        (activity as BasicActivity)
            .homeToolbar
            .setOnMenuItemClickListener {
                onOptionsItemSelected(it)
        }
    }

    /**
     * Gets user's data and displays in editTexts
     */
    private fun displayUserData() {
        userProfileViewModel.getUserData(currentUser!!.uid).observe(this, Observer { user ->
            binding.apply {
                presentationEditText.setText(user.presentation)
                nameEditText.setText(user.name)
                ageEditText.setText(user.age)
                countryEditText.setText(user.country)
            }
        })
    }

    /**
     * Gets data entered by user
     */
    private fun updateDataListener() {
            val presentation = presentationEditText.text.toString()
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val country = countryEditText.text.toString()

            if (validateUserData(presentation, name, age, country) && Validators.validProfileData(
                    presentation,
                    name,
                    age,
                    country
                )) {
               updateUserInformation(presentation, name, age, country)
            }
    }

    /**
     * Checks is user's profile data are correct
     */
    private fun  validateUserData(
        presentation: String,
        name: String,
        age: String,
        country:  String
    ): Boolean{
        var flag = true

        if(!Validators.validatePresentationLength(presentation)){
            binding.presentationEditText.error = getString(R.string.presentation_length_error)
            flag = false
        }
        if(!Validators.validateName(name)){
            binding.nameEditText.error = getString(R.string.short_name_error)
            flag = false
        }
        if(!Validators.validateAge(age)){
            binding.ageEditText.error = getString(R.string.age_error)
            flag = false
        }
        if(!Validators.validateCountryLength(country)){
            binding.countryEditText.error = getString(R.string.country_error)
            flag = false
        }

        return  flag
    }

    /**
     * Updates user information in firebase database
     */
    private fun updateUserInformation(
            presentation: String,
            name: String,
            age: String,
            country: String
    ){
        val progressBar = customDialog.progressDialog(this.context!!, getString(R.string.updating_information))

        databaseReference.document(currentUser!!.uid).apply {
            update("presentation",  presentation)
            update("name",  name)
            update("age",  age)
            update("country",  country)
                .addOnSuccessListener {
                    progressBar.cancel()
                    messaging.showToast("success", getString(R.string.information_updated_successfully))
                }
                .addOnFailureListener{
                    progressBar.cancel()
                    messaging.showToast("error", getString(R.string.something_went_wrong_try_again))
                }
        }
    }

    /**
     * Shows dialog to update email
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
     * Updates email in Firebase database
     */
    private fun updateEmail(email: String){
        currentUser!!.updateEmail(email)
            .addOnCompleteListener {
                databaseReference.document(currentUser.uid).update("email" , email)
                messaging.showToast("success", getString(R.string.email_updated_successfully))
            }
            .addOnFailureListener {
                messaging.showToast("error", getString(R.string.failed_to_update_email))
            }
    }

    /**
     * Handle menu actions
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.updateDataMenu -> {
                updateDataListener()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
