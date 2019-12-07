package com.example.chatonme.adapters

import com.example.chatonme.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_message_to_item.view.*

class MessageToAdapter(private val message: String): Item<GroupieViewHolder>(){

    override fun getLayout(): Int = R.layout.chat_message_to_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textMessageTo.text = message

    }

}
