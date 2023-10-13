package com.example.chat_app.splash

import androidx.lifecycle.ViewModel
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.model.User
import com.example.chat_app.firestore.usersdao.DAO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewModel : ViewModel() {

    val splashViewEvents = SingleLiveEvent<SplashViewEvents>()
    fun checkLoginUser() {
        if (Firebase.auth.currentUser == null) {
            splashViewEvents.postValue(SplashViewEvents.GoToLogin)
        } else {
            DAO.getUser(
                uid = Firebase.auth.currentUser?.uid
            ) {
                if (it.isSuccessful) {
                    var u = it.result.toObject(User::class.java)
                    SessionProvider.user = u
                    splashViewEvents.postValue(SplashViewEvents.GoToHome)
                } else {
                    splashViewEvents.postValue(SplashViewEvents.GoToLogin)
                }
            }


        }


    }
}