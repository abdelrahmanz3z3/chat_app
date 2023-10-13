package com.example.chat_app.firestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    var id: String? = null,
    var roomName: String? = null,
    var roomDescription: String? = null,
    var categoryId: Int? = null,
    var ownerId: String? = null,
    var listOfUsers: MutableList<String>? = null
) : Parcelable {
    companion object {
        val roomCollectionName = "Rooms"
    }
}
