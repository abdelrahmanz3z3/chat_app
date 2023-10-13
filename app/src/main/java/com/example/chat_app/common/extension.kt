package com.example.news_app.dialogextension

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import com.example.chat_app.common.Message

fun Fragment.showMessage(
    message: Message
): AlertDialog {
    val alert = AlertDialog.Builder(requireContext())
    alert.setMessage(message.message)
    alert.setPositiveButton(message.posMessage, message.posAction)
    alert.setNegativeButton(message.negMessage, message.negAction)
    return alert.show()
}


fun Activity.showMessage(
    message: Message
): AlertDialog {
    val alert = AlertDialog.Builder(this)
    alert.setMessage(message.message)
    alert.setPositiveButton(message.posMessage, message.posAction)
    alert.setNegativeButton(message.negMessage, message.negAction)
    return alert.show()
}

fun Activity.showLoadingDialog(
    message: Message?
): ProgressDialog {
    val alert = ProgressDialog(this)
    alert.setMessage(message?.message)
    alert.setCancelable(message?.isCancelable!!)
    return alert
}

fun Fragment.showLoadingDialog(
    message: Message?
): ProgressDialog {
    val alert = ProgressDialog(requireContext())
    alert.setMessage(message?.message)
    alert.setCancelable(message?.isCancelable!!)
    return alert
}