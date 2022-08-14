package com.audacoding.pasarlaut.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel: BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn get() = _isLoggedIn as LiveData<Boolean>

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
                        password
                    )
                }
            }.addOnFailureListener {
                _onLoading.postValue(false)
                _onError.postValue(it.message)
            }
    }

    private fun doLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _onSuccess.postValue(true)
            }.addOnFailureListener {
                _onLoading.postValue(false)
                _onError.postValue(it.message)
            }
    }
}