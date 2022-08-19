package com.audacoding.pasarlaut.models

data class Order(
    val uid: String = "",
    val productId: String = "",
    val productName: String = "-",
    val imgUrl: String = "",
    val price: Int = 0,
    val address: String = "-",
    val qty: String = "",
    val paymentMethod: String = "-",
    val sendingMethod: String = "-",
    val orderDate: Long = 0
)
