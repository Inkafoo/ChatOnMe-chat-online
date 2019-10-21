package com.example.chatonme

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.data.model.User


class UserListAdapter(val context: Context) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var users = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_profile_list_item, parent, false))
    }

    override fun getItemCount(): Int = users.size

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val user = users[position]
        holder.emailTextView.text = user.email.toString()
        holder.nameTextView.text = user.name.toString()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)!!
        val emailTextView = itemView.findViewById<TextView>(R.id.userEmailTextView)!!
        val profileImage = itemView.findViewById<ImageView>(R.id.profilePhotoImageView)!!
    }

    internal fun setUsers(surveys: List<User>) {
        this.users = surveys
        notifyDataSetChanged()
    }
}