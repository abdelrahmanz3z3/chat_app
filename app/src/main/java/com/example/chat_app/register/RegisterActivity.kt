package com.example.chat_app.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app.databinding.ActivityRegisterBinding
import com.example.chat_app.home.HomeActivity
import com.example.chat_app.login.LoginActivity
import com.example.news_app.dialogextension.showMessage

class RegisterActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActivity()
        observeLiveData()
    }

    private fun initActivity() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun observeLiveData() {
        viewModel.message.observe(this) { showMessage(it) }
        viewModel.registerViewEvents.observe(this, ::handelEvents)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun handelEvents(registerViewEvents: RegisterViewEvents) {
        when (registerViewEvents) {
            RegisterViewEvents.GoToLogin -> navigateToLogin()
            RegisterViewEvents.GoToHome -> navigateToHome()
        }

    }


}