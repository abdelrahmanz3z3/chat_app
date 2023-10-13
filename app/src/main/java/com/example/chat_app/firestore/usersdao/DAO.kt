package com.example.chat_app.firestore.usersdao

import com.example.chat_app.firestore.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DAO {
    fun getUserCollection(): CollectionReference {
        val firestore = Firebase.firestore
        return firestore.collection("users")
    }

    fun CreateUser(user: User, onCompleteListener: OnCompleteListener<Void>) {
        val docRef = getUserCollection().document(user.id ?: "")

        docRef.set(user).addOnCompleteListener(onCompleteListener)
    }

    fun getUser(uid: String?, onCompleteListener: OnCompleteListener<DocumentSnapshot>) {
        getUserCollection().document(uid!!).get().addOnCompleteListener(onCompleteListener)
    }

    fun updateUser(uid: String?, roomId: String?, onCompleteListener: OnCompleteListener<Void>) {
        getUserCollection().document(uid!!)
            .update("listOfJoinedRoomsId", FieldValue.arrayUnion(roomId))
            .addOnCompleteListener(onCompleteListener)
    }

    fun removeRoomFromUserList(
        uid: String,
        rid: String?,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        getUserCollection()
            .document(uid)
            .update("listOfJoinedRoomsId", FieldValue.arrayRemove(rid))
            .addOnCompleteListener(onCompleteListener)
    }

}