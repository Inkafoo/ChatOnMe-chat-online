package com.example.chatonme.views.extra


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentAddPostBinding
import com.example.chatonme.views.start.BasicActivity
import kotlinx.android.synthetic.main.fragment_main.*


class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater)



        return binding.root
    }






}
