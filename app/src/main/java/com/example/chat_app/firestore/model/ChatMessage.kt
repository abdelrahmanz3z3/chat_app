package com.example.chat_app.firestore.model

import android.icu.text.SimpleDateFormat
import com.example.chat_app.common.provider.SessionProvider
import com.google.firebase.Timestamp
import java.util.Locale

data class ChatMessage(
    var id: String? = null,
    var content: String? = null,
    var dateTime: Timestamp? = Timestamp.now(),
    var senderName: String? = SessionProvider.user?.fullName,
    var senderId: String? = SessionProvider.user?.id,
    var roomId: String? = null
) {
    companion object {
        val collectionName = "Messages"
    }

    fun formatTime(): String {
        var simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return simpleDateFormat.format(dateTime?.toDate())
    }
}
