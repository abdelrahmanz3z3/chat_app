package com.example.chat_app.addroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.addroom.categoryModel.CategoryModel
import com.example.chat_app.common.Message
import com.example.chat_app.common.RoomId
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.roomsdao.RoomsDao
import com.example.chat_app.firestore.usersdao.DAO

class AddRoomViewModel : ViewModel() {
    var roomNameLiveData = MutableLiveData<String?>()
    var roomDescriptionLiveData = MutableLiveData<String?>()
    var roomNameError = MutableLiveData<String?>()
    var roomDescriptionError = MutableLiveData<String?>()
    val list = CategoryModel.getCategories()
    private var selectedCategory = list[0]
    var showLoadingDialog = SingleLiveEvent<Message?>()
    var showDialog = SingleLiveEvent<Message?>()
    var roomViewEvents = SingleLiveEvent<RoomViewEvents>()

    private fun valid(): Boolean {
        var isValid = true
        if (roomNameLiveData.value.isNullOrBlank()) {
            roomNameError.postValue("Enter a valid name, please")
            isValid = false
        } else {
            roomNameError.postValue(null)
        }
        if (roomDescriptionLiveData.value.isNullOrBlank()) {
            roomDescriptionError.postValue("Enter a valid name, please")
            isValid = false
        } else {
            roomDescriptionError.postValue(null)
        }
        return isValid
    }

    fun getSelectedItem(position: Int) {
        selectedCategory = list[position]
    }

    fun createRoom() {
        if (!valid()) return
        showLoadingDialog.postValue(Message("Loading...", isCancelable = false))

        RoomsDao.createRoomDocument(
            title = roomNameLiveData.value!!,
            desc = roomDescriptionLiveData.value!!,
            ownerId = SessionProvider.user?.id!!,
            listOfUsers = mutableListOf(SessionProvider.user?.id!!),
            categoryId = selectedCategory.id ?: 0
        ) {
            if (it.isSuccessful) {
                updateUserJoinedRooms()

            }
        }
    }

    private fun updateUserJoinedRooms() {
        DAO.updateUser(uid = SessionProvider.user?.id, roomId = RoomId.roomId) {
            if (it.isSuccessful) {
                showLoadingDialog.postValue(null)
                SessionProvider.user?.listOfJoinedRoomsId?.add(RoomId.roomId!!)
                showDialog.postValue(
                    Message(
                        "Room Added Successfully",
                        posMessage = "OK",
                        posAction = { dialog, _ ->
                            roomViewEvents.postValue(RoomViewEvents.GoToHome)
                            dialog.dismiss()
                        })
                )

            }
        }
    }

}