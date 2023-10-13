package com.example.chat_app.datails

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_app.databinding.ActivityDetailsBinding
import com.example.chat_app.firestore.model.Room
import com.example.news_app.dialogextension.showLoadingDialog
import com.example.news_app.dialogextension.showMessage

class DetailsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityDetailsBinding
    var loadingDialog: ProgressDialog? = null
    val viewModel: DetailsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViews()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.showLoadingDialog.observe(this) {
            if (it == null) {
                loadingDialog?.dismiss()
                loadingDialog = null
                return@observe
            }
            loadingDialog = showLoadingDialog(it)
            loadingDialog?.show()
        }
        viewModel.showDialog.observe(this) {
            showMessage(it!!)
        }
        viewModel.detailsViewEvents.observe(this, ::handelEvents)
    }

    private fun handelEvents(detailsViewEvents: DetailsViewEvents?) {
        when (detailsViewEvents) {
            DetailsViewEvents.GoToHome -> navigateToHome()
            else -> {}
        }
    }

    private fun navigateToHome() {
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initViews() {
        var i = intent
        var room = i.getParcelableExtra("room") as Room?
        viewBinding.item = room
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewModel.getRoomId(room?.id)
    }

}