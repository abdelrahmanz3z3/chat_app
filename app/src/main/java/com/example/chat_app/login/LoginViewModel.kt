package com.example.chat_app.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.common.Message
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.model.User
import com.example.chat_app.firestore.usersdao.DAO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    var email = MutableLiveData<String?>()
    var password = MutableLiveData<String?>()
    var emailError = MutableLiveData<String?>()
    var passwordError = MutableLiveData<String?>()
    var message = SingleLiveEvent<Message>()
    var loginViewEvents = SingleLiveEvent<LoginViewEvents>()
    var showLoading = MutableLiveData<Boolean>()
    private fun valid(): Boolean {
        var isValid = true
        if (email.value.isNullOrBlank()) {
            emailError.postValue("Enter Your Email, Please")
            isValid = false
        } else {
            emailError.postValue(null)
        }
        if (password.value.isNullOrBlank()) {
            passwordError.postValue("Enter a valid password, Please")
            isValid = false
        } else {
            passwordError.postValue(null)

        }
        return isValid
    }

    fun Login() {
        if (!valid()) return
        showLoading.postValue(true)
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email.value!!, password.value!!).addOnCompleteListener {
            if (it.isSuccessful) {
                Start(it.result.user?.uid!!)

            } else {
                showLoading.postValue(false)
                message.postValue(
                    Message(message = it.exception?.localizedMessage,
                        posMessage = "ok",
                        posAction = { dialog, _ -> dialog.dismiss() })
                )

            }
        }
    }

    private fun Start(uid: String) {
        DAO.getUser(
            uid
        ) { task ->
            if (task.isSuccessful) {
                showLoading.postValue(false)
                var user = task.result.toObject(User::class.java)
                SessionProvider.user = user
                loginViewEvents.postValue(LoginViewEvents.GoToHome)

            } else {
                message.postValue(
                    Message(message = task.exception?.localizedMessage,
                        posMessage = "ok",
                        posAction = { dialog, _ -> dialog.dismiss() })
                )

            }

        }

    }

    fun dontHaveAccount() {
        loginViewEvents.postValue(LoginViewEvents.GoToRegister)
    }

}