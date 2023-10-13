package com.example.chat_app.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat_app.common.Message
import com.example.chat_app.common.SingleLiveEvent
import com.example.chat_app.common.provider.SessionProvider
import com.example.chat_app.firestore.model.User
import com.example.chat_app.firestore.usersdao.DAO
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    var fullNmeLiveData = MutableLiveData<String?>()
    var email = MutableLiveData<String?>()
    var password = MutableLiveData<String?>()
    var confirmPassword = MutableLiveData<String?>()
    var fullNmeError = MutableLiveData<String?>()
    var emailError = MutableLiveData<String?>()
    var passwordError = MutableLiveData<String?>()
    var confirmPasswordError = MutableLiveData<String?>()
    var message = SingleLiveEvent<Message>()
    var registerViewEvents = SingleLiveEvent<RegisterViewEvents>()
    var showLoading = MutableLiveData<Boolean>()
    private fun valid(): Boolean {
        var isValid = true
        if (fullNmeLiveData.value.isNullOrBlank()) {
            fullNmeError.postValue("Enter Your Name, Please")
            isValid = false
        } else {
            fullNmeError.postValue(null)

        }
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

        if (confirmPassword.value.isNullOrBlank() || confirmPassword.value != password.value) {
            confirmPasswordError.postValue("Password doesn't match")
            isValid = false
        } else {
            confirmPasswordError.postValue(null)
        }
        return isValid
    }

    fun CreateAccount() {
        if (!valid()) return
        showLoading.postValue(true)
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email.value!!, password.value!!).addOnCompleteListener {
            if (it.isSuccessful) {
                insertUserInFireStore(it.result.user?.uid!!)

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

    private fun insertUserInFireStore(uid: String) {
        val user = User(id = uid, fullName = fullNmeLiveData.value, email = email.value)
        DAO.CreateUser(user) { task ->
            if (task.isSuccessful) {
                showLoading.postValue(false)
                message.postValue(
                    Message(message = "Registration completed successfully",
                        posMessage = "ok",
                        posAction = { dialog, _ ->
                            dialog.dismiss()
                            SessionProvider.user = user
                            registerViewEvents.postValue(RegisterViewEvents.GoToLogin)
                        })
                )

            } else {
                message.postValue(
                    Message(message = task.exception?.localizedMessage,
                        posMessage = "ok",
                        posAction = { dialog, _ -> dialog.dismiss() })
                )

            }

        }

    }

    fun alreadyHaveAccount() {
        registerViewEvents.postValue(RegisterViewEvents.GoToLogin)
    }
}