package com.audacoding.pasarlaut.view.user.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.audacoding.pasarlaut.models.Chat
import com.audacoding.pasarlaut.view.BaseViewModel
import com.google.firebase.firestore.Query

class UserChatViewModel: BaseViewModel() {
    val chats = MutableLiveData<List<Chat>>()

    private val chatIds = mutableSetOf<String>()

    fun getChats(username: String) {
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
                            username == it.buyerName &&
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