package com.audacoding.pasarlaut.view.user.checkout

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
}