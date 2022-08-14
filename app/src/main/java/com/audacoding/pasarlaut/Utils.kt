package com.audacoding.pasarlaut

import android.content.Context
import android.view.View
import android.widget.Toast

fun showToast(context: Context, msg: String) {
    if(msg.isNotEmpty()) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}