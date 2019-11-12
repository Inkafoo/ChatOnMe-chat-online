package com.example.chatonme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatonme.R
import com.example.chatonme.models.User

class FriendsList(private val context: Context) : RecyclerView.Adapter<FriendsList.ViewHolder>() {
    private var user = emptyList<User>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_profile_list_item, parent, false))
    }

    override fun getItemCount(): Int = user.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)!!
        val emailTextView = itemView.findViewById<TextView>(R.id.userEmailTextView)!!
        val profileImageView = itemView.findViewById<ImageView>(R.id.profilePhotoImageView)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val user = user[position]
        holder.emailTextView.text = user.email
        holder.nameTextView.text = user.name


    }

    internal fun setUsers(user: List<User>) {
        this.user = user
        notifyDataSetChanged()
    }




}