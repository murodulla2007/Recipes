package com.example.recipesapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipesapi.MainScreen
import com.example.recipesapi.OneRecipeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "main",
//    startDestination: String = "oneRecipe/27",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            MainScreen(navController)
        }
        composable("oneRecipe/{id}") {
            val id = it.arguments?.getString("id")
            OneRecipeScreen(navController, id!!.toInt())
        }

    }
}