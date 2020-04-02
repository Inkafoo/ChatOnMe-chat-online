package com.example.chatonme.views.extra

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentAddPostBinding
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.POSTS_REFERENCE
import com.example.chatonme.helpers.Validators
import com.example.chatonme.models.Post
import com.example.chatonme.views.start.BasicActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import java.util.*

class AddPostFragment : Fragment() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referencePosts = firebaseDatabase.getReference(POSTS_REFERENCE)
    private val customDialog: CustomDialog by inject()
    private val messaging: Messaging by inject()
    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(inflater)

        setToolbarMenu()

        return binding.root
    }

    /**
     * Checks inputted post's data
     */
    private fun checkDataListener() {
        val title = postTitleEditText.text.toString()
        val description = postDescriptionEditText.text.toString()

        if(validatePostDetails(title, description)) {
            uploadPost(title, description)
        }
    }

    /**
     * Displays errors when data are incorrect
     */
    private fun validatePostDetails(title: String, description: String): Boolean {
        var flag = true
        if (!Validators.validatePostTitleLength(title)) {
            binding.postTitleEditText.error = getString(R.string.title_post_error)
            flag = false
        }
        if (!Validators.validatePostDescriptionLength(description)) {
            binding.postDescriptionEditText.error = getString(R.string.post_description)
            flag = false
        }

        return flag
    }

    /**
     * Sends post to firebase database
     */
    private fun uploadPost(title: String, description: String) {
        val progressBar = customDialog.progressDialog(this.context!!, getString(R.string.uploading_post))
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val currentTime = Calendar.getInstance().time.toString()

        val post = Post(
            title,
            description,
            currentUserId,
            currentTime
        )

        referencePosts.push().setValue(post)
            .addOnSuccessListener {
                progressBar.cancel()
                messaging.showToast("success", getString(R.string.posted_successfully))
            }
            .addOnFailureListener {
                progressBar.cancel()
                messaging.showToast("error", it.message.toString())
            }
    }

    /**
     * Inflates menu and set to toolbar
     */
    private fun setToolbarMenu() {
        (activity as BasicActivity).homeToolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    /**
     * Handle menu actions
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.postMenu -> {
                checkDataListener()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
