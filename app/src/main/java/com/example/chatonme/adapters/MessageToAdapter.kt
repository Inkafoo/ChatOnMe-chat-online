package com.example.chatonme.adapters

import android.view.View
import com.example.chatonme.R
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.helpers.FormatDate
import com.example.chatonme.models.ChatMessage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_message_to_item.view.*

class MessageToAdapter(
    private val message: ChatMessage,
    private val image: String,
    private val imageProcessing: ImageProcessing
): Item<GroupieViewHolder>(){

    override fun getLayout(): Int = R.layout.chat_message_to_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textMessageTo.text = message.text
        imageProcessing.setImage(image, viewHolder.itemView.userImageTo)

        viewHolder.itemView.textMessageTo.setOnClickListener {
            showMessageDate(viewHolder, message.date)
        }
    }

    private fun showMessageDate(viewHolder: GroupieViewHolder, date: Long) {
        val isVisible =  viewHolder.itemView.dateTextViewMessageTo.visibility
        viewHolder.itemView.dateTextViewMessageTo.text = FormatDate.getFormattedTime(date)

        if(isVisible == 0){
            viewHolder.itemView.dateTextViewMessageTo.visibility = View.GONE
        }else{
            viewHolder.itemView.dateTextViewMessageTo.visibility = View.VISIBLE
        }
    }

}
