package com.example.chatonme.models

class ChatMessage(

    val text: String,
    val fromId: String,
    val toId: String,
    val date: Long
){
    constructor() : this( "", "", "", 0)
}