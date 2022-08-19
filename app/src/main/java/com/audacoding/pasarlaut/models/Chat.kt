package com.audacoding.pasarlaut.models

data class Chat(
    val id: Long = -1,
    val nelayanName: String = "",
    val buyerName: String = "",
    val receiverId: String = "",
    val senderId: String = "",
    val roomId: String = "",
    val message: String = "",
    val sendAt: Long = 0,
    val productDetail: NelayanProduct = NelayanProduct(
        -1, "", "", "", "","", "", -1
    )
)
