package com.example.recipesapi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipesapi.model.Comments
import com.example.recipesapi.model.Recipe
import com.example.recipesapi.model.RecipeLiked

@Database(entities = [RecipeLiked::class, Comments::class], version = 2)

abstract class AppDatabase(): RoomDatabase() {


    abstract fun recipeDao(): RecipeDao
    abstract fun commentDao(): CommentDao

    companion object{
        const val DB_NAME = "lc_db"
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Example: Adding a new column to a table
////            database.execSQL("ALTER TABLE students ADD COLUMN birthdate TEXT")
//                database.execSQL("ALTER TABLE Enrollment ADD COLUMN enrollmentDate TEXT NOT NULL DEFAULT 'undefined'")
//            }
//        }

        private var instance: AppDatabase? = null

        fun getIntance(context: Context): AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }

    }

}