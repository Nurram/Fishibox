@file:Suppress("PropertyName")

package com.audacoding.pasarlaut.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class BaseViewModel: ViewModel() {
    protected val _onLoading = MutableLiveData(false)
    val onLoading: LiveData<Boolean> get() = _onLoading

    protected val _onError = MutableLiveData("")
    val onError: LiveData<String> get() = _onError

    protected val _onSuccess = MutableLiveData(false)
    val onSuccess: LiveData<Boolean> get() = _onSuccess

    protected val _auth = FirebaseAuth.getInstance()
    protected val _storage = FirebaseStorage.getInstance()
    protected val _firestore = FirebaseFirestore.getInstance()
}