package com.example.chatonme.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.chatonme.R
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item


class MessageToAdapter: Item<GroupieViewHolder>(){

    override fun getLayout(): Int = R.layout.chat_message_to_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}
