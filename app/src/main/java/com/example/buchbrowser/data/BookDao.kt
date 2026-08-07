package com.example.buchbrowser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

// app/src/main/java/com/example/buchbrowser/data/BookDao.kt
// BookDao
//
// Created by Arjang Khademi on 03.08.2026

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBooks(books: List<BookEntity>)

    @Query("SELECT * FROM seen_books ORDER BY title ASC")
    fun getSeenBooksFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM seen_books ORDER BY title ASC")
    suspend fun getSeenBooks(): List<BookEntity>

    @Query("SELECT * FROM seen_books WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM seen_books WHERE isInReadingList = 1 ORDER BY title ASC")
    fun getReadingListBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM seen_books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM seen_books WHERE id = :bookId)")
    suspend fun isBookSeen(bookId: String): Boolean

    @Query("DELETE FROM seen_books")
    suspend fun clearSeenBooks()
}
