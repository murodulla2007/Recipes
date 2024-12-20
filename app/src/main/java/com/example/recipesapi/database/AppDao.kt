package com.example.recipesapi.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.recipesapi.model.Comments
import com.example.recipesapi.model.RecipeLiked
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeLiked")
    fun getAll(): Flow<List<RecipeLiked>>

    @Query("SELECT * FROM RecipeLiked WHERE recipe_id = :id")
    fun getById(id: Int): RecipeLiked

    @Insert
    fun insertLiked(recipeLiked: RecipeLiked)

    @Delete
    fun deleteLiked(recipeLiked: RecipeLiked)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM Comments")
    fun getAll(): Flow<List<Comments>>

    @Query("SELECT * FROM Comments WHERE recipe_id = :id")
    fun getById(id: Int): Flow<List<Comments>>

    @Insert
    fun insertComment(recipeLiked: Comments)

    @Delete
    fun deleteComment(recipeLiked: Comments)
}
