package com.example.chat_app.addroom

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chat_app.R
import com.example.chat_app.addroom.categoryModel.CategoryAdapter
import com.example.chat_app.databinding.ActivityAddRoomBinding
import com.example.news_app.dialogextension.showLoadingDialog
import com.example.news_app.dialogextension.showMessage

class AddRoomActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityAddRoomBinding
    lateinit var adapter: CategoryAdapter
    lateinit var viewModel: AddRoomViewModel
    var alert: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_room)
        initViews()
        observeLiveData()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this)[AddRoomViewModel::class.java]
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        adapter = CategoryAdapter(viewModel.list)
        viewBinding.spinner.adapter = adapter
        viewBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.getSelectedItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun navigateToHome() {
        finish()
    }

    private fun observeLiveData() {
        viewModel.roomViewEvents.observe(this, ::handelEvents)
        viewModel.showLoadingDialog.observe(this) {
            if (it == null) {
                alert?.dismiss()
                alert = null
                return@observe
            }
            alert = showLoadingDialog(it)
            alert?.show()


        }
        viewModel.showDialog.observe(this) {
            showMessage(it!!)

        }
    }

    private fun handelEvents(roomViewEvents: RoomViewEvents?) {
        when (roomViewEvents) {
            RoomViewEvents.GoToHome -> navigateToHome()
            else -> {}
        }
    }
}