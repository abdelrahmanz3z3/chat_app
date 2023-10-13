package com.example.chat_app.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.model.Room
import com.example.chat_app.firestore.model.User
import com.example.chat_app.firestore.roomsdao.RoomsDao

class HomeViewModel : ViewModel() {

    val tabsTitle = listOf<String>("My Rooms", "Browser")
    var homeViewEvents = SingleLiveEvent<HomeViewEvents>()
    var roomsData = MutableLiveData<MutableList<Room>?>()
    var showLoading = MutableLiveData<Boolean>()
    var room: Room? = null
    fun navigateToAddRoomActivity() {
        homeViewEvents.postValue(HomeViewEvents.GoToAddRoomActivity)
    }

    fun loadData(u: User? = null) {
        showLoading.postValue(true)
        RoomsDao.getRoomsList(u?.id) {
            showLoading.postValue(false)
            if (!it.isSuccessful) {
                return@getRoomsList

            }
            var roomsList = it.result.toObjects(Room::class.java)
            roomsData.postValue(roomsList)


        }
    }

    fun roomSetter(item: Room) {
        this.room = item
    }

    fun checkJoining() {

        room?.let {
            val index = SessionProvider.user?.listOfJoinedRoomsId!!.indexOf(room?.id!!)
            if (index >= 0) {
                homeViewEvents.postValue(HomeViewEvents.GoToChatActivity)
                return
            }
            homeViewEvents.postValue(HomeViewEvents.GoToDetailsActivity)

        }
    }


}