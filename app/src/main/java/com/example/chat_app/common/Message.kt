package com.example.chat_app.common

import android.content.DialogInterface

data class Message(
    var message: String? = null,
    var posMessage: String? = null,
    var posAction: DialogInterface.OnClickListener? = null,
    var negMessage: String? = null,
    var negAction: DialogInterface.OnClickListener? = null,
    var isCancelable: Boolean = true
)
