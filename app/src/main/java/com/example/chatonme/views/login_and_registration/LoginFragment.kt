package com.example.chatonme.views.login_and_registration


import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatonme.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.chatonme.R
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.Validators
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {

    private val messaging: Messaging by inject()
    private val customDialog: CustomDialog by inject()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

        navigateToRegistrationFragment(binding.notMemberButton)
        loginUserListener(binding.loginButton)
        forgottenPasswordListener(binding.forgetPasswordButton)


        return binding.root
    }

    /**
     * Valid of credentials
     */
    private fun loginUserListener(view: View){
        RxView.clicks(view).map {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(validateUserCredentials(email, password)){
                requestFirebaseCredentialValidation(email, password)
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Login user
     */
    private fun requestFirebaseCredentialValidation(email: String, password: String) {
       val progressDialog = customDialog.progressDialog(this.context!!, getString(R.string.logging_in))
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
           if(it.isSuccessful){
               navigateToHome()
               progressDialog.cancel()
               messaging.showToast(getString(R.string.logged_in))
           }else{
               progressDialog.cancel()
               messaging.showToast(it.exception!!.message.toString())
           }
        }
    }


    /**
     * Handles forgotten password
     */
    private fun forgottenPasswordListener(view: View){
        RxView.clicks(view).map {
            customDialog.materialDialog(this.context!!).show {
                title(text = getString(R.string.enter_email_for_password_reset))
                input(
                    hint = getString(R.string.email),
                    waitForPositiveButton = true,
                    allowEmpty = false,
                    inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                )
                positiveButton(text = getString(R.string.request_password)){
                    requestPasswordReset(this.getInputField().text.toString())
                }
                negativeButton(text = getString(R.string.cancel)) {
                    this.dismiss()
                }
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Validate User credentials
     */
    private fun validateUserCredentials(email: String, password: String): Boolean {
        var flag = true
        if (!Validators.validateEmail(email)) {
            binding.emailEditText.error = getString(R.string.email_error)
            flag = false
        }
        if (!Validators.validatePasswordLength(password)) {
            binding.passwordEditText.error = getString(R.string.password_length_error)
            flag = false
        }
        return flag
    }

    /**
     * handle password reset
     */
    private fun requestPasswordReset(email: String){
        val progressDialog = customDialog.progressDialog(this.context!!, getString(R.string.sending_email))
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                progressDialog.cancel()
                messaging.showToast(getString(R.string.email_sent))
            }else{
                progressDialog.cancel()
                messaging.showToast(getString(R.string.something_went_wrong_try_again))
            }
        }
    }

    /**
     * Navigate to registerFragment
     */
    private fun navigateToRegistrationFragment(view: View){
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Navigate to connectBottomNavigationFragment
     */
    private fun navigateToHome(){
        when (findNavController().currentDestination!!.id) {
            R.id.loginFragment -> findNavController().navigate(R.id.action_loginFragment_to_connectBottomNavigationFragment)
        }
    }

    /**
     * Set color of part notMemberButton text
     */
    private fun setNotMemberButtonTextColor() {
        val spannable = SpannableString(getString(R.string.not_a_member_sign_up_now))
        spannable.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        notMemberButton.text = spannable
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setNotMemberButtonTextColor()
    }
}
