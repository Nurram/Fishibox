package com.audacoding.pasarlaut.view.user.chatDetail

import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.firestore.Query

class ChatDetailViewModel: BaseViewModel() {
    val chats = MutableLiveData<List<Chat>>()

    fun getUid(): String = _auth.uid!!

    fun getChat(username: String, productId: Long) {
        _onLoading.postValue(true)

        _firestore.collection("chats")
            .orderBy("sendAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    _onLoading.postValue(false)
                    _onError.postValue(error.message)
                } else {
                    _onLoading.postValue(false)

                    val uid = _auth.uid!!
                    val docs = value!!.documents
                    val filteredChat = docs.filter { doc ->
                        val docObject = doc.toObject(Chat::class.java)!!
                        (docObject.receiverId == uid ||
                                docObject.senderId == uid) &&
                                docObject.productDetail.id == productId

                    }

                    val chats = arrayListOf<Chat>()
                    filteredChat.forEach { snapshot ->
                        chats.add(snapshot.toObject(Chat::class.java)!!)
                    }

                    this.chats.postValue(chats)
                }
            }
    }

    fun sendChat(chat: Chat) {
        _firestore.collection("chats").add(chat)
            .addOnFailureListener {
                _onError.postValue(it.message)
            }
    }
}