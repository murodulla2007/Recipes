package com.example.recipesapi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipesapi.model.Recipe
import com.example.recipesapi.networking.fetchAllData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navHostController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val focusedColor = Color(0xFF6200EE)  // Accent color when focused
    val unfocusedColor = Color(0xFFE0E0E0)

    var recipeList: List<Recipe> by remember { mutableStateOf(emptyList()) }
    recipeList = fetchAllData()

    val filteredRecipeList = if (searchQuery.isEmpty()) {
        recipeList
    } else {
        recipeList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
    Log.d("TAG after seach", "MainScreen: $filteredRecipeList")

    Scaffold(
        topBar = {

            TopAppBar(
//                modifier = Modifier.height(100.dp),
                modifier = Modifier.padding(bottom = 10.dp).height(130.dp),
                title = {
                    Text(
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                        text = "Recipes",
                        fontSize = 30.sp,
                    ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    if (isSearchVisible){
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search Recipes",
                                    style = TextStyle(color = Color.Gray),
                                    modifier = Modifier,
//                        textAlign = TextAlign.End
//
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Gray,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Icon",
                                    tint = Color.Gray,
                                    modifier = Modifier.clickable {
                                        if (searchQuery.isEmpty()){
                                            isSearchVisible = false
                                        }
                                        if (searchQuery.isNotEmpty()) {
                                            searchQuery = ""
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
//                        .padding(top = 20.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(
                                    1.dp,
                                    if (searchQuery.isNotEmpty()) focusedColor else unfocusedColor,
                                    RoundedCornerShape(12.dp)
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,  // Remove the default underline
                                unfocusedIndicatorColor = Color.Transparent,  // Remove the default underline
                                containerColor = Color.White,
                                cursorColor = focusedColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                            singleLine = true
                        )
                    } else {
                        // Show the search button when isSearchVisible is false
                        IconButton(
                            onClick = { isSearchVisible = true },
                            modifier = Modifier
                                .padding(top = 20.dp, end = 20.dp)
                                .size(35.dp)
                                .align(alignment = Alignment.CenterVertically)
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
//                    .padding(top = 30.dp)
                    .padding(padding)
            ) {
                // Search Bar
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                label = { Text("Search Recipes") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRecipeList) { recipe ->
                        RecipeItem(recipe, navHostController)
                    }
                }

            }
        }
    }



//    LazyColumn {
//        items(filteredContacts, key = { it.id }) { contact ->
//            ContactItem(
//                contact = contact,
//                onDelete = {
//                    appDataBase.contactUserDAO().deleteContactUser(contact)
//                    contacts = appDataBase.contactUserDAO().getAll()
//
//                },
//                onEdit = {
//                    navHostController.navigate("create/${contact.id}")
//                },
//                navHostController
//            )
//        }
//    }
}

@Composable
fun RecipeItem(recipe: Recipe, navHostController: NavHostController){

    Card(modifier = Modifier
        .height(250.dp)
        .clickable {
            navHostController.navigate("oneRecipe/${recipe.id}")
//            OneRecipeScreen(navHostController = navHostController, recipe = recipe)
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .placeholder(android.R.drawable.ic_menu_gallery) // Resource for placeholder
                    .error(android.R.drawable.stat_notify_error)       // Resource for error
//                    .crossfade(true)                     // Enable crossfade
                    .transformations() // Optional: Transform image
                    .build(),
                contentDescription = "Image from URL",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(0.7f)
                    .clip(RoundedCornerShape(topStart = 20.dp))
                    .background(Color(0xFF161616).copy(alpha = 0.85f))

            ) {
                Text(text = "${recipe.name}",
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                )
            }

        }

    }
}