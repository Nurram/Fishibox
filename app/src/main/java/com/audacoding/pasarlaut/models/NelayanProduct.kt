package com.audacoding.pasarlaut.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NelayanProduct(
    val id: Long = -1,
    val uid: String = "",
    val productName: String = "",
    val stock: String = "",
    val desc: String = "",
    val price: String = "",
    val imgUrl: String = "",
    val createdAt: Long = 0
): Parcelable
