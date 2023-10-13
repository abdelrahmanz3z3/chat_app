package com.example.chat_app.common

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.chat_app.R
import com.example.chat_app.addroom.categoryModel.CategoryModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("txt")
fun txt(text: TextView, s: String) {
    text.text = "Join ${s} Room"

}

@BindingAdapter("img")
fun bindImage(imageView: ImageView, id: Int) {
    var i = CategoryModel.getImage(id)
    imageView.setImageResource(i)
}

@BindingAdapter("Error")
fun BindError(textInputLayout: TextInputLayout, error: String? = null) {
    textInputLayout.error = error
}

@BindingAdapter("Join")
fun joinedRoom(btn: MaterialButton, roomId: String) {
    btn.text = "Join room"
    btn.setBackgroundColor(ContextCompat.getColor(btn.context, R.color.my_light_primary))
    btn.tag = "join"
}