package com.audacoding.pasarlaut.models

data class Chats(
    val receiverName: String,
    val lastMessage: String,
    val chats: List<Chat> = listOf()
)
