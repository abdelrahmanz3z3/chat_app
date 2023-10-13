package com.example.chat_app.firestore.messagedao

import com.example.chat_app.firestore.model.ChatMessage
import com.example.chat_app.firestore.roomsdao.RoomsDao
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference

object MessageDao {
    fun getMessageCollection(roomId: String): CollectionReference {
        return RoomsDao.getRoomsCollection().document(roomId).collection(ChatMessage.collectionName)
    }

    fun insertMessage(message: ChatMessage, onCompleteListener: OnCompleteListener<Void>) {
        val collRef = getMessageCollection(message.roomId!!)
        val messageRef = collRef.document()
        message.id = messageRef.id
        messageRef.set(message).addOnCompleteListener(onCompleteListener)
    }

}