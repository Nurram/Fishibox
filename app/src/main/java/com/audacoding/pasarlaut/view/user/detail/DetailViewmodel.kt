package com.audacoding.pasarlaut.view.user.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.BaseViewModel

class DetailViewModel: BaseViewModel() {
    private val _productAdded = MutableLiveData<Map<String, Any>>()
    val productAdded get() = _productAdded as LiveData<Map<String, Any>>

    private val _productFavorite = MutableLiveData(false)
    val productFavorite get() = _productFavorite as LiveData<Boolean>

    private val _onDelete = MutableLiveData(false)
    val onDelete get() = _onDelete as LiveData<Boolean>

    private var favId = ""

    fun checkIsFav(id: Long) {
        _firestore.collection("favorites")
            .whereEqualTo("productId", id)
            .whereEqualTo("uid", _auth.uid)
            .addSnapshotListener { value, error ->
                    if(error != null) {
                        _onError.postValue(error.message)
                    } else {
                        _onLoading.postValue(false)

                        if(!value!!.isEmpty) {
                            favId = value.documents.first().id
                            _productFavorite.postValue(true)
                        } else {
                            _productFavorite.postValue(false)
                        }
                    }
                }
    }
    fun addToFav(product: NelayanProduct) {
        _onLoading.postValue(true)

        val uid = _auth.uid!!
        _firestore.collection("favorites").add(
            mapOf(
                "uid" to uid,
                "productId" to product.id,
                "product" to product
            )
        ).addOnSuccessListener {
            _onLoading.postValue(false)
            _productAdded.postValue(
                mapOf(
                    "value" to true,
                    "desc" to "Favorite"
                )
            )
        }.addOnFailureListener {
            _onError.postValue(it.message)
        }
    }

    fun addToCart(product: NelayanProduct) {
        _onLoading.postValue(true)

        val uid = _auth.uid!!
        _firestore.collection("carts")
            .whereEqualTo("productId", product.id)
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    _firestore.collection("carts").add(
                        mapOf(
                            "uid" to uid,
                            "productId" to product.id,
                            "product" to product
                        )
                    ).addOnSuccessListener {
                        _onLoading.postValue(false)
                        _productAdded.postValue(
                            mapOf(
                                "value" to true,
                                "desc" to "Cart"
                            )
                        )
                    }.addOnFailureListener { e ->
                        _onError.postValue(e.message)
                    }
                } else {
                    _onLoading.postValue(false)
                    _productAdded.postValue(
                        mapOf(
                            "value" to true,
                            "desc" to "Cart"
                        )
                    )
                }
            }.addOnFailureListener {
                _onError.postValue(it.message)
            }
    }

    fun removeFromFav() {
        _onLoading.postValue(true)

        _firestore.collection("favorites")
            .document(favId).delete().addOnSuccessListener {
                _onDelete.postValue(true)
                _onLoading.postValue(false)
            }.addOnFailureListener {
                _onError.postValue(it.message)
            }
    }
}