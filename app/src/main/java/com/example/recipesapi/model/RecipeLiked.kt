package com.example.recipesapi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeLiked(
    @PrimaryKey(autoGenerate = true)
    val likeID: Int = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
)
