package com.example.chat_app.addroom.categoryModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.chat_app.databinding.CategoryItemBinding

class CategoryAdapter(var list: List<CategoryModel>?) : BaseAdapter() {
    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(position: Int): Any {

        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return list!![position].id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        if (convertView == null) {
            val itemBinding =
                CategoryItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            viewHolder = ViewHolder(itemBinding)
            itemBinding.root.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.bind(list!![position])
        return viewHolder.itemBinding.root
    }

    class ViewHolder(val itemBinding: CategoryItemBinding) {
        fun bind(cat: CategoryModel) {
            itemBinding.image.setImageResource(cat.imageResourceId!!)
            itemBinding.title.text = cat.title
        }
    }
}