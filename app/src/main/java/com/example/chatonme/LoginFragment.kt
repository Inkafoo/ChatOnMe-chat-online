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
import androidx.navigation.fragment.findNavController
import com.example.chatonme.databinding.FragmentLoginBinding
import com.example.chatonme.databinding.FragmentRegisterBinding
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

        navigateToRegistrationFragment(binding.notMemberButton)

        return binding.root
    }

    private fun navigateToRegistrationFragment(view: View){
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }.subscribe()
    }

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
