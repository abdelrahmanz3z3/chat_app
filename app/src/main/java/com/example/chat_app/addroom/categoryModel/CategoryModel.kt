package com.example.chat_app.addroom.categoryModel

import com.example.chat_app.R

data class CategoryModel(
    val id: Int? = null,
    val title: String? = null,
    val imageResourceId: Int? = null
) {
    fun getI(id: Int): Int {
        return when (id) {
            1 -> R.drawable.sports
            2 -> R.drawable.movies
            3 -> R.drawable.music
            else -> {
                R.drawable.sports
            }
        }
    }

    companion object {
        fun getCategories() = listOf<CategoryModel>(
            CategoryModel(1, "Sports", R.drawable.sports),
            CategoryModel(2, "Movies", R.drawable.movies),
            CategoryModel(3, "Music", R.drawable.music)
        )

        fun getImage(id: Int): Int {
            return when (id) {
                1 -> R.drawable.sports
                2 -> R.drawable.movies
                3 -> R.drawable.music
                else -> {
                    R.drawable.sports
                }
            }
        }

    }
}