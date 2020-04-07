package com.example.chatonme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatonme.R
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.models.Post

class PostListAdapter(
    private val context: Context,
    private val imageProcessing: ImageProcessing
) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    private var postList = emptyList<Post>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorImageView = itemView.findViewById<ImageView>(R.id.authorImageView)!!
        val authorNameTextView = itemView.findViewById<TextView>(R.id.authorNameTextView)!!
        val postTimeTextView = itemView.findViewById<TextView>(R.id.postTimeTextView)!!
        val postDescriptionTextView = itemView.findViewById<TextView>(R.id.postDescriptionTextView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.post_item, parent, false))
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]

        holder.apply {
            authorNameTextView.text = post.authorId
            postTimeTextView.text = post.publicationTime
            postDescriptionTextView.text = post.description
        }

        //imageProcessing.setImage(post.authorId, holder.authorImageView)
    }

    internal fun setPosts(posts: List<Post>) {
        postList = posts
        notifyDataSetChanged()
    }

}