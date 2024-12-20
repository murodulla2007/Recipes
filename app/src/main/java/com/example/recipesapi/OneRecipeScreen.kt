package com.example.recipesapi

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipesapi.database.AppDatabase
import com.example.recipesapi.model.Comments
import com.example.recipesapi.model.Recipe
import com.example.recipesapi.model.RecipeLiked
import com.example.recipesapi.networking.Retrofit
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRecipeScreen(navHostController: NavHostController, id: Int) {
//    val recipe = navHostController.previousBackStackEntry?.savedStateHandle?.get<Recipe>("recipe")

    val context = LocalContext.current
    val appDataBase = AppDatabase.getIntance(context)

    val defaultStartPadding = 20.dp
    val defaultTopPadding = 10.dp
    var liked by remember { mutableStateOf(false) }

    val likedList by appDataBase.recipeDao().getAll().collectAsState(initial = emptyList())
    val commentList by appDataBase.commentDao().getById(id).collectAsState(initial = emptyList())
    var isLiked = appDataBase.recipeDao().getById(id)
    if (likedList.filter { it.recipeId == id }.isNotEmpty()) {
        liked = true
    }

    var newComment by remember { mutableStateOf("") }
    val focusedColor = Color(0xFF6200EE)  // Accent color when focused
    val unfocusedColor = Color(0xFFE0E0E0)


    var isLoading by remember { mutableStateOf(true) }
    var recipe0: Recipe? by remember { mutableStateOf(null) }
    val call = Retrofit.api.getRecipeById(id)
    call.enqueue(object : Callback<Recipe> {
        override fun onResponse(
            call: retrofit2.Call<Recipe>,
            response: Response<Recipe>
        ) {
            if (response.isSuccessful) {
                val recipesResponse = response.body()
                recipesResponse?.let {
                    recipe0 = it
                    Log.d("TAGbek", "onResponse: ${it.name}")
                }
                isLoading = false
            } else {
                println("Error: ${response.code()} - ${response.message()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<Recipe>, t: Throwable) {
            Log.d("TAGbek", "onFailure: failed")
        }
    })



    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Center the progress indicator
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp) // Adjust the size of the progress indicator
            )
        }
    } else {
        val recipe = requireNotNull(recipe0) { "Recipe cannot be null" }
//        var recipe = Recipe(10, 10, "aads", "hard", 1, "", listOf("potato", "tomato"), listOf("cook", "boil", "freeze"), listOf("dinner"), "Pizza", 10, 5.0, 100, 1, listOf("Italy"), 100)
//        recipe?.let { recipe ->
//            // Display the recipe details
//            Text(text = recipe.name)
//        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),

            ) {
                item {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recipe.image)
//                            .data(android.R.drawable.ic_menu_gallery)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_notify_error)
                                .build(),
                            contentDescription = "Image from URL",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )
                        IconButton(
                            onClick = {
//                    navHostController.navigate("main")
                                liked = !liked
                                Log.d("TAGali", "OneRecipeScreen: $liked")
                                if (liked && likedList.filter { it.recipeId == recipe.id }.isEmpty()) {
                                    appDataBase.recipeDao().insertLiked(RecipeLiked(recipeId = recipe.id))
                                }
                                else if (!liked && likedList.filter { it.recipeId == recipe.id }.isNotEmpty() && isLiked != null) {
                                    appDataBase.recipeDao().deleteLiked(isLiked)
                                    Log.d("TAGbekali", "OneRecipeScreen: DELETE")
                                }
                            },
                            modifier = Modifier
                                .padding(
                                    bottom = defaultTopPadding + 40.dp,
                                    end = defaultStartPadding
                                )
                                .size(60.dp)
                                .alpha(0.9f)
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "",
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxSize()
                                    .padding(10.dp),
                                tint = if (liked) Color.Red else Color.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 50.dp))
                                .background(Color.White)
                                .fillMaxWidth()
                                .height(25.dp)
                                .align(Alignment.BottomCenter)

                        )
                    }
                }
//            Image(painter = painterResource(id = android.R.drawable.ic_menu_gallery),
//                    contentDescription = "Image from URL",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth())
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = defaultTopPadding, start = defaultStartPadding)
                            .offset(y = (-6).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(recipe.tags) {
                            Text(
                                text = "#$it",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .background(Color.Black)
                                    .padding(vertical = 6.dp, horizontal = 10.dp),


                                color = Color.White
                            )
                        }

                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = defaultTopPadding, start = defaultStartPadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = recipe.name,
                            modifier = Modifier,
                            color = Color.Black,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold
                        )


                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = defaultTopPadding,
                                start = defaultStartPadding * 2,
                                end = defaultStartPadding * 2
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${recipe.difficulty}",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .background(
                                    if (recipe.difficulty == "Easy")
                                        Color(0xFF4CAF50)
                                    else
                                        Color(0xFFFF9800)
                                )
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            color = Color.White
                        )
                        Row(modifier = Modifier) {
                            Row(
                                modifier = Modifier
                                    .padding(end = defaultStartPadding),
                                horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                                    contentDescription = ""
                                )
                                Text(text = "${recipe.cookTimeMinutes + recipe.prepTimeMinutes}m")
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "",
                                    tint = Color(0xFFFFBF00)
                                )
                                Text(text = "${recipe.rating} (${recipe.reviewCount})")
                            }
                        }

                    }

                }
                item {
                    Column(modifier = Modifier
                        .padding(top = defaultTopPadding + 10.dp, start = defaultStartPadding + 10.dp)) {
                        Text(
                            text = "Ingredients",
                            modifier = Modifier,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                items(recipe.ingredients) {
                    Text(
                        text = "- $it",
                        modifier = Modifier
                            .padding(top = defaultTopPadding/4, start = defaultStartPadding + 24.dp, end = defaultStartPadding + 24.dp),
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(top = defaultTopPadding + 10.dp, start = defaultStartPadding + 10.dp)) {
                        Text(
                            text = "Instructions",
                            modifier = Modifier,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }
                items(recipe.instructions) {
                    Text(
                        text = "- $it",
                        modifier = Modifier
                            .padding(top = defaultTopPadding/4, start = defaultStartPadding + 24.dp, end = defaultStartPadding + 24.dp),
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(top = defaultTopPadding + 10.dp, start = defaultStartPadding + 10.dp, end = defaultStartPadding + 10.dp)) {
                        Text(
                            text = "Comments",
                            modifier = Modifier,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                        TextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            placeholder = {
                                Text(
                                    text = "Add comment",
                                    style = TextStyle(color = Color.Gray),
                                    modifier = Modifier,
//                        textAlign = TextAlign.End
//
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Done Icon",
                                    tint = Color.Gray,
                                    modifier = Modifier.clickable {
                                        val date = Date(System.currentTimeMillis())
                                        val formatter = SimpleDateFormat("HH:mm:ss/dd:MM:yyyy", Locale.getDefault())
                                        val dateString = formatter.format(date)
                                        if (newComment.isNotEmpty()){
                                            appDataBase.commentDao().insertComment(Comments(comment = newComment, date = dateString, recipeId = id))
                                            newComment = ""
                                        }

                                    }
                                )
                            },
                            modifier = Modifier
//                                .padding(top = defaultTopPadding/4, start = defaultStartPadding + 24.dp, end = defaultStartPadding + 24.dp)
                                .fillMaxWidth()
//                        .padding(top = 20.dp)
                                .heightIn(min = 50.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(
                                    1.dp,
                                    if (newComment.isNotEmpty()) focusedColor else unfocusedColor,
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
                        )
                    }
                }
                items(commentList.reversed()){
                    CommentItem(comment = it)
                }
                item {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.1f)
                    )
                }
            }
            IconButton(
                onClick = {
                    navHostController.navigate("main")

//                    if (liked && likedList.filter { it.recipeId == recipe.id }.isEmpty()) {
//                        appDataBase.recipeDao().insertLiked(RecipeLiked(recipeId = recipe.id))
//                    }
//                    else if (!liked && likedList.filter { it.recipeId == recipe.id }.isNotEmpty() && isLiked != null) {
//                        appDataBase.recipeDao().deleteLiked(isLiked)
//                        Log.d("TAGbekali", "OneRecipeScreen: DELETE")
//                    }
                },
                modifier = Modifier
                    .padding(top = defaultTopPadding + 40.dp, start = defaultStartPadding + 10.dp)
                    .size(50.dp)
                    .alpha(0.9f)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize(),
                    tint = Color.Black
                )
            }

        }
    }
//    Log.d("TAGlola", "OneRecipeScreen: ${recipe0}")
//    Text(text = recipe!!.name)
}


@Composable
fun CommentItem(comment: Comments){

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFDADADA))
    ) {
        Column(modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp),) {
            Text(
                text = "${getRelativeTime(comment.date)}",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3F51B5)
            )
            Text(text = "${comment.comment}",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
            )
        }



    }
}
fun parseDateToTime(dateString: String): String {
    val originalFormat = SimpleDateFormat("HH:mm:ss/dd:MM:yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val date = originalFormat.parse(dateString)

    return outputFormat.format(date)
}

fun getRelativeTime(dateString: String): String {
    val formatter = SimpleDateFormat("HH:mm:ss/dd:MM:yyyy", Locale.getDefault())

    val savedDate = formatter.parse(dateString)
    val savedTimeMillis = savedDate?.time ?: return "Invalid date"

    val currentTimeMillis = System.currentTimeMillis()

    val differenceMillis = currentTimeMillis - savedTimeMillis

    val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(differenceMillis)
    val days = TimeUnit.MILLISECONDS.toDays(differenceMillis)

    return when {
        seconds < 60 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
        days >= 7 -> "${days/7} week${if (days/7 > 1) "s" else ""} ago"
        days >= 31 -> "${days/30} month${if (days/30 > 1) "s" else ""} ago"
        else -> "A long time ago"
//        else -> "More than a week ago"
    }
}