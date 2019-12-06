package com.example.chatonme.adapters

import android.content.Context
import android.provider.Settings.Global.getString
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.example.chatonme.R
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.models.Users
import es.dmoral.toasty.Toasty


class UserListAdapter(
    private val context: Context,
    private val imageProcessing: ImageProcessing,
    private val customDialog: CustomDialog
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var users = emptyList<Users>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_profile_list_item, parent, false))
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)!!
        val emailTextView = itemView.findViewById<TextView>(R.id.userEmailTextView)!!
        val profileImageView = itemView.findViewById<ImageView>(R.id.profilePhotoImageView)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val user = users[position]
        holder.emailTextView.text = user.email
        holder.nameTextView.text = user.name
        imageProcessing.setImage(user.image.toString(), holder.profileImageView)

        holder.itemView.setOnClickListener {
            showActionDialog(holder.itemView, user)
        }
    }

    internal fun setUsers(users: List<Users>) {
        this.users = users
        notifyDataSetChanged()
    }

    private fun showActionDialog(view: View, selectedUser: Users?){
        val pickedUser= bundleOf("selectedUser" to selectedUser)
        customDialog.materialDialog(this.context).show {
            listItems(R.array.userListAdapterActionsArray){ dialog, index, text ->
                when(index){
                    0 ->  view.findNavController().navigate(R.id.action_usersListFragment_to_friendProfileFragment)
                    1 ->  view.findNavController().navigate(R.id.action_usersListFragment_to_chatFragment, pickedUser)
                }
            }
        }
    }


}