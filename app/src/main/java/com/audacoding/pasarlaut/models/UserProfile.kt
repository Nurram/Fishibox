package com.audacoding.pasarlaut.models

data class UserProfile(
    val name: String? = "-",
    val username: String = "",
    val address: String? = "-",
    val phone: String? = "-",
    val imgUrl: String? = null
)