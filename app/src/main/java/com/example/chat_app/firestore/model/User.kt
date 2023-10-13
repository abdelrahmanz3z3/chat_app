package com.example.chat_app.firestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val fullName: String? = null,
    val email: String? = null,
    var listOfJoinedRoomsId: MutableList<String>? = mutableListOf(),
    var porfilePicture: String? = null
) : Parcelable
