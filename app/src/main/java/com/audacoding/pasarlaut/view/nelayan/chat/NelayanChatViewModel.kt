package com.audacoding.pasarlaut.view.nelayan.chat

import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.firestore.Query

class NelayanChatViewModel: BaseViewModel() {
    val chats = MutableLiveData<List<Chat>>()

    private val chatIds = mutableSetOf<String>()

    fun getChats() {
        _firestore.collection("chats").orderBy("sendAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error != null) {
                    _onError.postValue(error.message)
                } else {
                    val chats = arrayListOf<Chat>()
                    value!!.documents.forEach {
                        chatIds.add(it.getString("roomId")!!)
                        chats.add(
                            it.toObject(Chat::class.java)!!
                        )
                    }

                    val newChat = arrayListOf<Chat>()
                    chatIds.forEach { id ->
                        val filteredChat = chats.filter {
                            it.productDetail.uid == _auth.uid!! &&
                            it.roomId == id
                        }

                        if(filteredChat.isNotEmpty()) {
                            val firstFiltered = filteredChat.first()
                            newChat.add(firstFiltered)
                        }
                    }

                    this.chats.postValue(newChat)
                }
            }
    }
}