package com.example.chat_app.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_app.R
import com.example.chat_app.home.HomeActivity
import com.example.chat_app.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeLiveData()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkLoginUser()
        }, 2000)
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun observeLiveData() {
        viewModel.splashViewEvents.observe(this, ::handelEvents)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun handelEvents(splashViewEvents: SplashViewEvents?) {
        when (splashViewEvents) {
            SplashViewEvents.GoToHome -> navigateToHome()
            SplashViewEvents.GoToLogin -> navigateToLogin()
            else -> {}
        }
    }


}