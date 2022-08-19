package com.audacoding.pasarlaut.view.user.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.BaseViewModel

class DashboardViewModel: BaseViewModel() {
    private var _products = MutableLiveData<List<NelayanProduct>>()
    val products get() = _products as LiveData<List<NelayanProduct>>

    fun getProducts() {
        _onLoading.postValue(true)
        val products = arrayListOf<NelayanProduct>()

        _firestore.collection("products").addSnapshotListener { value, error ->
            if(error != null) {
                _onLoading.postValue(false)
                _onError.postValue(error.message)
            } else {
                products.clear()

                value!!.documents.forEach {
                    products.add(
                        NelayanProduct(
                            it.getLong("id")!!,
                            it.getString("userId") ?: "-1",
                            it.getString("productName") ?: "-",
                            it.getString("stock") ?: "0",
                            it.getString("desc") ?: "-",
                            it.getString("price") ?: "0",
                            it.getString("imgUrl") ?: "",
                            it.getLong("createdAt") ?: 0
                        )
                    )
                }

                _onLoading.postValue(false)
                _products.postValue(products)
            }
        }
    }
}