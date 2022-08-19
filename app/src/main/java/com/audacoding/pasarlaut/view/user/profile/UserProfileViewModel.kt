package com.audacoding.pasarlaut.view.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.firestore.DocumentSnapshot

class UserProfileViewModel: BaseViewModel() {
    private var _userSnapshot = MutableLiveData<DocumentSnapshot>()
    val userSnapshot: LiveData<DocumentSnapshot> get() = _userSnapshot

    fun getProfile() {
        _onLoading.postValue(true)

        val uid = _auth.uid!!
        val userRef = _firestore.collection("users")

        userRef.whereEqualTo("uid", uid).addSnapshotListener { value, error ->
            if(error != null) {
                _onLoading.postValue(false)
                _onError.postValue(error.message)
            } else {
                val user = value!!.documents.first()
                _userSnapshot.postValue(user)

                _onLoading.postValue(false)
            }
        }
    }

    fun setLoading(loading: Boolean) {
        _onLoading.postValue(loading)
    }

    fun signOut() {
        _auth.signOut()
        _onSuccess.postValue(true)
    }
}