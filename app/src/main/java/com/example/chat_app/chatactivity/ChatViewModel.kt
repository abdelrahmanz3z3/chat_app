package com.example.chat_app.chatactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.common.Message
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.messagedao.MessageDao
import com.example.chat_app.firestore.model.ChatMessage
import com.example.chat_app.firestore.model.Room
import com.example.chat_app.firestore.roomsdao.RoomsDao
import com.example.chat_app.firestore.usersdao.DAO
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query

class ChatViewModel : ViewModel() {
    lateinit var room: Room
    var contentLiveData = MutableLiveData<String?>()
    var showLoadingDialog = SingleLiveEvent<Message?>()
    var showDialog = SingleLiveEvent<Message?>()
    var chatViewEvents = SingleLiveEvent<ChatViewEvents>()
    var messagesLiveData = SingleLiveEvent<MutableList<ChatMessage>>()

    fun roomSetter(room: Room) {
        this.room = room
        MessageDao
            .getMessageCollection(roomId = room.id!!)
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                val list = mutableListOf<ChatMessage>()
                value?.documentChanges?.forEach {
                    when (it.type) {
                        DocumentChange.Type.ADDED -> {
                            val message = it.document.toObject(ChatMessage::class.java)
                            list.add(message)
                        }

                        DocumentChange.Type.MODIFIED -> {}
                        DocumentChange.Type.REMOVED -> {}
                        else -> {}
                    }
                }
                messagesLiveData.value = list
            }

    }

    fun sendMessage() {
        if (contentLiveData.value.isNullOrBlank()) return
        val message = ChatMessage(
            content = contentLiveData.value,
            roomId = room.id
        )
        contentLiveData.postValue(null)
        MessageDao
            .insertMessage(message) {
                if (it.isSuccessful) {
                    return@insertMessage
                }
                handelError(
                    it.exception?.localizedMessage ?: "Something went wrong ,try again later"
                )
            }
    }

    fun disjoinRooms() {
        showDialog.postValue(
            Message(
                "Are you sure you want to leave ?",
                posMessage = "Yes",
                posAction = { dialog, _ ->
                    dialog.dismiss()
                    showLoadingDialog.postValue(Message("Loading...", isCancelable = false))
                    RoomsDao.removeUserFromRoomsList(SessionProvider.user?.id!!, room.id!!) {
                        if (it.isSuccessful) {
                            disjoinUser()
                        } else {
                            handelError("Some thing went wrong ,try again later")
                        }
                    }
                }, negMessage = "No", negAction = { dialog, _ ->
                    dialog.dismiss()
                })
        )

    }

    private fun disjoinUser() {
        DAO.removeRoomFromUserList(SessionProvider.user?.id!!, room.id!!) {
            if (it.isSuccessful) {
                showLoadingDialog.postValue(null)
                SessionProvider.user?.listOfJoinedRoomsId?.remove(room.id!!)
                showDialog.postValue(
                    Message(
                        "You Leaved the Room",
                        posMessage = "OK",
                        posAction = { dialog, _ ->
                            chatViewEvents.postValue(ChatViewEvents.GoToHome)
                            dialog.dismiss()
                        })
                )

            } else {
                handelError("Some thing went wrong ,try again later")
            }
        }
    }

    fun handelError(message: String) {
        showDialog.postValue(
            Message(
                message,
                posMessage = "Ok",
                posAction = { dilog, _ -> dilog.dismiss() })
        )

    }

}