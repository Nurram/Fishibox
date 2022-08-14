package com.audacoding.pasarlaut.view.register

import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel: BaseViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun register(username: String, email: String, password: String, userType: String) {
        _onLoading.postValue(true)

        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    doRegister(username, email, password, userType)
                } else {
                    _onLoading.postValue(false)
                    _onError.postValue("Username already used!")
                }
            }.addOnFailureListener {
                _onLoading.postValue(false)
                _onError.postValue(it.message)
            }
    }

    private fun doRegister(username: String, email: String, password: String, userType: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            if(it.user != null) {
                storeUserData(username, email, it.user!!.uid, userType)
            } else {
                _onLoading.postValue(false)
                _onError.postValue("Something happened!")
            }
        }.addOnFailureListener {
            _onLoading.postValue(false)
            _onError.postValue(it.message)
        }
    }

    private fun storeUserData(username: String, email: String, uid: String, userType: String) {
        firestore.collection("users").document(uid).set(mapOf(
            "username" to username,
            "email" to email,
            "userType" to userType,
            "uid" to uid
        )).addOnSuccessListener {
            _onSuccess.postValue(true)
        }.addOnFailureListener {
            _onLoading.postValue(false)
            _onError.postValue(it.message)
        }
    }
}