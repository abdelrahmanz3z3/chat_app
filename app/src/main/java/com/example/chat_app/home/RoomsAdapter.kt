package com.example.chat_app.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.chat_app.addroom.categoryModel.CategoryModel
import com.example.chat_app.databinding.RoomItemBinding
import com.example.chat_app.firestore.model.Room

class RoomsAdapter(var list: MutableList<Room>?) : Adapter<RoomsAdapter.ViewHolder>() {
    class ViewHolder(var itemBinding: RoomItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemBinding =
            RoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.img.setImageResource(CategoryModel.getImage(list?.get(position)?.categoryId!!))
        holder.itemBinding.tit.text = list?.get(position)?.roomName ?: ""
        //-------------------------------------------------------------
        setOnRoomClickListener?.let {
            holder.itemBinding.root.setOnClickListener { view ->
                it.onClick(position, list!![position])
            }
        }

    }

    var setOnRoomClickListener: SetOnRoomClickListener? = null

    fun interface SetOnRoomClickListener {
        fun onClick(position: Int, item: Room)
    }

    fun bind(it: MutableList<Room>?) {
        this.list = it
        notifyDataSetChanged()
    }


}