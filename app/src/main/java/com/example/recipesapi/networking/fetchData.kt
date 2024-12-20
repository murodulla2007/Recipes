package com.example.recipesapi.networking

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.recipesapi.model.Recipe
import com.example.recipesapi.model.Recipes
import retrofit2.Callback
import retrofit2.Response

@Composable
fun fetchAllData(): List<Recipe> {
    var recipeList: List<Recipe> by remember { mutableStateOf(emptyList()) }
    val recipes = Retrofit.api.getRecipes()
    recipes.enqueue(object : Callback<Recipes> {
        override fun onResponse(
            call: retrofit2.Call<Recipes>,
            response: Response<Recipes>
        ) {
            if (response.isSuccessful) {
                val recipesResponse = response.body()
                recipesResponse?.let {
                    recipeList = it.recipes
                    Log.d("TAGbek", "onResponse: ${recipeList.size}")
                }
            } else {
                println("Error: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<Recipes>, t: Throwable) {
            Log.d("TAGbekFETCH", "onFailure: failed ${t.message}")
        }
    })
    return recipeList

}

//@Composable
//fun fetchDataById(id: Int): Recipe {
//    var recipe: Recipe? by remember { mutableStateOf(null) }
//    val recipes = Retrofit.api.getRecipeById(id)
//    recipes.enqueue(object : Callback<Recipe> {
//        override fun onResponse(
//            call: retrofit2.Call<Recipe>,
//            response: Response<Recipe>
//        ) {
//            if (response.isSuccessful) {
//                val recipesResponse = response.body()
//                recipesResponse?.let {
//                    recipe = it
//                    Log.d("TAGbek", "onResponse: ${it.name}")
//                }
//            } else {
//                println("Error: ${response.code()} - ${response.message()}")
//            }
//        }
//
//        override fun onFailure(call: retrofit2.Call<Recipe>, t: Throwable) {
//            Log.d("TAGbek", "onFailure: failed")
//        }
//    })
//    return recipe
//}