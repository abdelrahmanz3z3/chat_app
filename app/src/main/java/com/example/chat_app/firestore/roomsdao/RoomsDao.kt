package com.example.chat_app.firestore.roomsdao

import com.example.chat_app.common.RoomId
import com.example.chat_app.firestore.model.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object RoomsDao {
    fun getRoomsCollection(): CollectionReference {
        val fireStore = Firebase.firestore
        return fireStore.collection(Room.roomCollectionName)
    }

    fun createRoomDocument(
        title: String,
        desc: String,
        ownerId: String,
        categoryId: Int,
        listOfUsers: MutableList<String>?,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val collection = getRoomsCollection()
        val docRef = collection.document()
        RoomId.roomId = docRef.id
        val room = Room(
            id = docRef.id,
            roomName = title,
            roomDescription = desc,
            categoryId = categoryId,
            ownerId = ownerId,
            listOfUsers = listOfUsers,
        )
        docRef
            .set(room)
            .addOnCompleteListener(onCompleteListener)
    }

    fun getRoomsList(u: String? = null, onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        if (u != null) {
            getRoomsCollection()
                .whereArrayContains("listOfUsers", u)
                .get()
                .addOnCompleteListener(onCompleteListener)
        } else {
            getRoomsCollection()
                .get()
                .addOnCompleteListener(onCompleteListener)
        }
    }

    fun updateRoomsUsers(
        rid: String,
        uid: String,
        addOnCompleteListener: OnCompleteListener<Void>
    ) {
        getRoomsCollection().document(rid).update("listOfUsers", FieldValue.arrayUnion(uid))
            .addOnCompleteListener(addOnCompleteListener)
    }

    fun removeUserFromRoomsList(
        uid: String,
        rid: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        getRoomsCollection()
            .document(rid)
            .update("listOfUsers", FieldValue.arrayRemove(uid))
            .addOnCompleteListener(onCompleteListener)

    }


}