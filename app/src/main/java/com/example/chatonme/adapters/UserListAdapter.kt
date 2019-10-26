package com.example.chatonme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatonme.R
import com.example.chatonme.models.Users


class UserListAdapter(val context: Context) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var users = emptyList<Users>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_profile_list_item, parent, false))
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)!!
        val emailTextView = itemView.findViewById<TextView>(R.id.userEmailTextView)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val user = users[position]
        holder.emailTextView.text = user.email
        holder.nameTextView.text = user.name
    }


    internal fun setUsers(users: List<Users>) {
        this.users = users
        notifyDataSetChanged()
    }
}