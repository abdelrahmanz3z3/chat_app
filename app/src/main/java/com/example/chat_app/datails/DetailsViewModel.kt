package com.example.chat_app.datails

import androidx.lifecycle.ViewModel
import com.example.chat_app.common.Message
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.roomsdao.RoomsDao
import com.example.chat_app.firestore.usersdao.DAO

class DetailsViewModel : ViewModel() {

    var showLoadingDialog = SingleLiveEvent<Message?>()
    var showDialog = SingleLiveEvent<Message?>()
    var detailsViewEvents = SingleLiveEvent<DetailsViewEvents>()
    var roomId: String? = null
    fun joinRoom() {
        showLoadingDialog.postValue(Message("Loading...", isCancelable = true))
        RoomsDao.updateRoomsUsers(roomId!!, SessionProvider.user?.id!!) {
            if (it.isSuccessful) {
                joined()
            }
        }
    }

    private fun joined() {
        DAO.updateUser(SessionProvider.user?.id!!, roomId!!) {
            if (it.isSuccessful) {
                showLoadingDialog.postValue(null)
                SessionProvider.user?.listOfJoinedRoomsId?.add(roomId!!)
                showDialog.postValue(
                    Message(
                        "You Joined the Room",
                        posMessage = "OK",
                        posAction = { dialog, _ ->
                            detailsViewEvents.postValue(DetailsViewEvents.GoToHome)
                            dialog.dismiss()
                        })
                )

            }
        }
    }


    fun getRoomId(id: String?) {
        roomId = id
    }
}