package com.example.chatonme.views.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chatonme.R
import com.example.chatonme.adapters.PostListAdapter
import com.example.chatonme.databinding.FragmentHomeBinding
import com.example.chatonme.helpers.POSTS_REFERENCE
import com.example.chatonme.models.PostsListViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val postsListViewModel: PostsListViewModel by inject()
    private val postListAdapter: PostListAdapter by inject()
    private val databaseReference = Firebase.firestore.collection(POSTS_REFERENCE)
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        binding.recyclerPostView.adapter = postListAdapter

        postsListViewModel.showPostList(databaseReference).observe(this, Observer {
            postListAdapter.setPosts(it)
        })

        navigateToAddPostListener(binding.addPostFloatingButton)


        return binding.root
    }


    /**
     * Navigates to AddPostFragment
     */
    private fun navigateToAddPostListener(view: View){
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_homeFragment_to_addPostFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }
}
