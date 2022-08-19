package com.audacoding.pasarlaut.view.user.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.BaseViewModel

class CartViewModel: BaseViewModel() {
    private val _favorites = MutableLiveData<List<NelayanProduct>>()
    val favorites get() = _favorites as LiveData<List<NelayanProduct>>

    private val favIds = arrayListOf<String>()

    fun getFavorites() {
        _onLoading.postValue(true)
        _firestore.collection("carts")
            .whereEqualTo("uid", _auth.uid)
            .addSnapshotListener { value, error ->
                _onLoading.postValue(false)

                if(error != null) {
                    _onError.postValue(error.message)
                } else {

                    val favorites = arrayListOf<NelayanProduct>()
                    value!!.documents.forEach { productData ->
                        favIds.add(productData.id)

                        val product = productData.get("product", NelayanProduct::class.java)!!
                        favorites.add(product)
                    }

                    _favorites.postValue(favorites)
                }
            }
    }

    fun removeCart(pos: Int) {
        _onLoading.postValue(true)

        _firestore.collection("carts").document(favIds[pos]).delete().addOnSuccessListener {
            _onLoading.postValue(false)
        }.addOnFailureListener {
            _onLoading.postValue(false)
            _onError.postValue(it.message)
        }
    }
}