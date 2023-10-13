package com.example.chat_app.chatactivity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat_app.R
import com.example.chat_app.databinding.ActivityChatBinding
import com.example.chat_app.firestore.model.Room
import com.example.news_app.dialogextension.showLoadingDialog
import com.example.news_app.dialogextension.showMessage

class ChatActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityChatBinding
    lateinit var viewModel: ChatViewModel
    val adapter = MessagesAdapter(mutableListOf())
    var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        observeLiveData()
    }

    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        val i = intent
        val room = (i.getParcelableExtra("room") as Room?)!!
        viewModel.roomSetter(room)
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.title = null
        viewBinding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.more)
        viewBinding.rec.adapter = adapter
        (viewBinding.rec.layoutManager as LinearLayoutManager).stackFromEnd = true
    }


    private fun observeLiveData() {
        viewModel.chatViewEvents.observe(this, ::handelEvents)
        viewModel.showDialog.observe(this)
        {
            showMessage(it!!)
        }
        viewModel.showLoadingDialog.observe(this) {
            if (it == null) {
                dialog?.dismiss()
                dialog = null
                return@observe
            }
            dialog = showLoadingDialog(it)
            dialog?.show()
        }
        viewModel.messagesLiveData.observe(this) {
            adapter.bindMessages(it!!)
            viewBinding.rec.smoothScrollToPosition(adapter.itemCount)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.l -> {
                viewModel.disjoinRooms()
                return true
            }
        }
        return onOptionsItemSelected(item)
    }

    private fun handelEvents(chatViewEvents: ChatViewEvents?) {
        when (chatViewEvents) {
            ChatViewEvents.GoToHome -> {
                finish()
            }

            else -> {}
        }
    }


}