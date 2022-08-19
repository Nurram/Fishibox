package com.audacoding.pasarlaut.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class LoginViewModel: BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _loginSuccess = MutableLiveData<Map<String, Any>>()
    val loginSuccess get() = _loginSuccess as LiveData<Map<String, Any>>

    fun checkUserLoggedIn() {
        _onSuccess.postValue(auth.currentUser != null)
    }

    fun login(username: String, password: String) {
        _onLoading.postValue(true)

        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    _onLoading.postValue(false)
                    _onError.postValue("Invalid email/password!")
                } else {
                    val userData = it.documents.first()
                    doLogin(
                        userData.get("email") as String,
                        password,
                        userData.getString("userType")!!,
                        username
                    )
                }
            }.addOnFailureListener {
                _onLoading.postValue(false)
                _onError.postValue(it.message)
            }
    }

    private fun doLogin(email: String, password: String, userType: String, username: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loginSuccess.postValue(mapOf(
                    "loginSuccess" to true,
                    "userType" to userType,
                    "username" to username
                ))
            }.addOnFailureListener {
                _onLoading.postValue(false)
                _onError.postValue(it.message)
            }
    }
}