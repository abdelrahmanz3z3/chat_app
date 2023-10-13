package com.example.chat_app.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app.databinding.ActivityLoginBinding
import com.example.chat_app.home.HomeActivity
import com.example.chat_app.register.RegisterActivity
import com.example.news_app.dialogextension.showMessage

class LoginActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initActivity()
        observeLiveData()
    }

    private fun initActivity() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun observeLiveData() {
        viewModel.message.observe(this) { showMessage(it) }
        viewModel.loginViewEvents.observe(this, ::handelEvents)
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun handelEvents(loginViewEvents: LoginViewEvents) {
        when (loginViewEvents) {
            LoginViewEvents.GoToRegister -> navigateToRegister()
            LoginViewEvents.GoToHome -> navigateToHome()
        }

    }
}
