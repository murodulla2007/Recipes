package com.example.recipesapi.model

data class Recipes(
    val limit: Int,
    val recipes: List<Recipe>,
    val skip: Int,
    val total: Int
)