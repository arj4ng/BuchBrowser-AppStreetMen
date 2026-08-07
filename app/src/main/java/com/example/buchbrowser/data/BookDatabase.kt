package com.example.buchbrowser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var instance: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "buch_browser_database",
                )
                .fallbackToDestructiveMigration()
                .build().also { database ->
                    instance = database
                }
            }
        }
    }
}
