package com.audacoding.pasarlaut.view.nelayan.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.BaseViewModel

class NelayanHomeViewModel: BaseViewModel() {
    private val _products = MutableLiveData<List<NelayanProduct>>(arrayListOf())
    val products: LiveData<List<NelayanProduct>> get() = _products

    fun getProducts() {
        _onLoading.postValue(true)

        val products = arrayListOf<NelayanProduct>()
        val uid = _auth.uid!!

        _firestore.collection("products")
            .whereEqualTo("userId", uid)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    _onLoading.postValue(false)
                    _onError.postValue(error.message)
                } else if(value != null && !value.isEmpty) {
                    products.clear()

                    value.documents.forEach {
                        val product = NelayanProduct(
                            it.id,
                            it.getString("userId")!!,
                            it.getString("productName")!!,
                            it.getString("stock")!!,
                            it.getString("desc")!!,
                            it.getString("imgUrl")!!)

                        products.add(product)
                    }

                    this._products.postValue(products)
                }

                _onLoading.postValue(false)
            }
    }
}