package com.example.recipesapi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comments(
    @PrimaryKey(autoGenerate = true)
    val commentId: Int = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
    @ColumnInfo(name = "comment")
    val comment: String,
    val date: String,
)