package com.example.recipesapi.networking

import com.example.recipesapi.model.Recipe
import com.example.recipesapi.model.Recipes
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object Retrofit {
    private const val BASE_URL = "https://dummyjson.com"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


}

interface ApiService {
    @GET("/recipes")
    fun getRecipes(): Call<Recipes>

    @GET("recipes/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

}