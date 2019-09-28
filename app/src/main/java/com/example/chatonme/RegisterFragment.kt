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
import com.example.chatonme.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)




        return binding.root
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
