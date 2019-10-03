package com.example.chatonme


import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatonme.databinding.FragmentRegisterBinding
import com.example.chatonme.helpers.Validators
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.concurrent.TimeUnit


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)

        navigateToLoginFragment(binding.alreadyRegisteredButton)
        registerUserListener()

        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    private fun registerUserListener(){
        RxView.clicks(binding.registerButton).map {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.passwordConfirmEditText.text.toString()

            if (validateUser(name, email, password, confirmPassword) && Validators.isValidUser(
                    name,
                    email,
                    password,
                    confirmPassword
                )){
                createUserOnFirebase(email, password)
            }
        }.throttleFirst(1, TimeUnit.SECONDS).subscribe()
    }

    private fun  validateUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean{
        var flag = true
        if(!Validators.validateName(name)){
            binding.nameEditText.error = getString(R.string.short_name_error)
            flag = false
        }
        if(!Validators.validateEmail(email)){
            binding.emailEditText.error = getString(R.string.email_error)
            flag = false
        }
        if(!Validators.validatePasswordLength(password)){
            binding.passwordEditText.error = getString(R.string.password_length_error)
            flag = false
        }
        if(!Validators.validatePasswordConfirmation(password, confirmPassword)){
            binding.passwordConfirmEditText.error = getString(R.string.password_mismatch_error)
            flag = false
        }

        return  flag
    }

    private fun createUserOnFirebase(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener {
            Toasty.error(this.context!!,"$it", Toasty.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            navigate()
        }
    }

    /**
     * Navigates to Login fragment from Registration fragment
     */
    private fun navigate() {
        when (findNavController().currentDestination!!.id) {
            R.id.registerFragment -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun navigateToLoginFragment(view: View){
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    private fun setRegisteredButtonTextColor(){
        val spannable = SpannableString(getString(R.string.already_member_login_me))
        spannable.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            17,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        alreadyRegisteredButton.text = spannable
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRegisteredButtonTextColor()
    }
}