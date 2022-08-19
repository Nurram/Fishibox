package com.audacoding.pasarlaut.view.user.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.Order
import com.audacoding.pasarlaut.view.BaseViewModel

class HistoryViewModel: BaseViewModel() {
    private val _orders = MutableLiveData<List<Order>>()
    val orders get() = _orders as LiveData<List<Order>>

    fun getHistories() {
        _onLoading.postValue(true)
        val uid = _auth.uid!!

        _firestore.collection("orders").whereEqualTo("uid", uid)
            .addSnapshotListener { value, error ->
                _onLoading.postValue(false)

                if(error != null) {
                    _onError.postValue(error.message)
                } else {
                    val orders = arrayListOf<Order>()
                    value!!.documents.forEach {
                        val order = it.toObject(Order::class.java)

                        if(order != null) {
                            orders.add(order)
                        }
                    }

                    _orders.postValue(orders)
                }
            }
    }
}