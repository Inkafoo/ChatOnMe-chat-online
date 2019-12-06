package com.example.chatonme.adapters

import com.example.chatonme.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MessageFromAdapter: Item<GroupieViewHolder>(){

    override fun getLayout(): Int = R.layout.chat_message_from_item


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}
