package com.example.chatonme.adapters

import android.view.View
import com.example.chatonme.R
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.helpers.FormatDate
import com.example.chatonme.models.ChatMessage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_message_from_item.view.*

class MessageFromAdapter (
    private val message: ChatMessage,
    private val image: String,
    private val imageProcessing: ImageProcessing
) : Item<GroupieViewHolder>(){


    override fun getLayout(): Int = R.layout.chat_message_from_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textMessageFrom.text = message.text
        imageProcessing.setImage(image, viewHolder.itemView.userImageFrom)

        viewHolder.itemView.textMessageFrom.setOnClickListener {
           showMessageDate(viewHolder, message.date)
        }

    }

    private fun showMessageDate(viewHolder: GroupieViewHolder, date: Long){
        val isVisible =  viewHolder.itemView.dateTextViewMessageFrom.visibility
        viewHolder.itemView.dateTextViewMessageFrom.text = FormatDate.getFormattedTime(date)

        if(isVisible == 0){
            viewHolder.itemView.dateTextViewMessageFrom.visibility = View.GONE
        }else{
            viewHolder.itemView.dateTextViewMessageFrom.visibility = View.VISIBLE
        }

    }

}
