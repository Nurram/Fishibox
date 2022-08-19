package com.audacoding.pasarlaut.view.user.checkout

import android.util.Log
import com.audacoding.pasarlaut.models.NelayanProduct
import com.audacoding.pasarlaut.view.BaseViewModel

class CheckoutViewModel: BaseViewModel() {

    fun saveOrder(data: MutableMap<String, Any>) {
        _onLoading.postValue(true)

        data["uid"] = _auth.uid!!
        _firestore.collection("products")
            .get().addOnSuccessListener {
                val doc = it.documents.filter { document ->
                    document.getLong("id").toString() == data["productId"]
                }
                val product = doc.first().toObject(NelayanProduct::class.java)
                val qty = data["qty"] as String
                val reducedQty = product!!.stock.toInt() - qty.toInt()

                if(reducedQty > 0) {
                    reduceStock(doc.first().id, reducedQty)
                    removeProductFromCart(doc.first().getLong("id")!!)

                    data["uid"] = _auth.uid!!
                    _firestore.collection("orders").add(data).addOnSuccessListener {
                        _onLoading.postValue(false)
                        _onSuccess.postValue(true)
                    }.addOnFailureListener { e ->
                        _onLoading.postValue(false)
                        _onError.postValue(e.message)
                    }
                } else {
                    _onLoading.postValue(false)
                    _onError.postValue("Stock telah habis!")
                }
            }
    }

    private fun reduceStock(id: String, qty: Int) {
        _firestore.collection("products").document(id)
            .update(mapOf(
                "stock" to qty.toString()
            ))
    }

    private fun removeProductFromCart(id: Long) {
        Log.d("TAG", "$id, ${_auth.uid}")
        _firestore.collection("carts")
            .whereEqualTo("uid", _auth.uid)
            .whereEqualTo("productId", id)
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    _firestore.collection("carts")
                        .document(it.first().id).delete()
                }
            }
            .addOnFailureListener {
                Log.d("TAG", it.message.toString())
            }
    }
}