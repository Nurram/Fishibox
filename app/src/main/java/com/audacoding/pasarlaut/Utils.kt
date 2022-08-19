package com.audacoding.pasarlaut

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

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

fun storeStringToPref(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences("pasarLaut", MODE_PRIVATE)
    sharedPref.edit().putString(key, value).apply()
}

fun getStringFromPref(context: Context, key: String): String? {
    val sharedPref = context.getSharedPreferences("pasarLaut", MODE_PRIVATE)
    return sharedPref.getString(key, null)
}

fun removeStringFromPref(context: Context, key: String) {
    val sharedPref = context.getSharedPreferences("pasarLaut", MODE_PRIVATE)
    sharedPref.edit().remove(key).apply()
}

fun setScreenTouchable(activity: Activity) {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}

fun setScreenUntouchable(activity: Activity) {
    activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}

fun millisToDate(millis: Long, format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val date = Date()
    date.time = millis

    return formatter.format(date)
}