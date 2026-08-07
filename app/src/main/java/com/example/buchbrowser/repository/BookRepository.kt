package com.example.buchbrowser.repository

import com.example.buchbrowser.data.Book
import kotlinx.coroutines.flow.Flow

// app/src/main/java/com/example/buchbrowser/repository/BookRepository.kt
// BookRepository
//
// Created by Arjang Khademi on 03.08.2026

interface BookRepository {

    suspend fun searchBooks(query: String): List<Book>

    suspend fun getRandomBook(): Book?

    fun getSeenBooksFlow(): Flow<List<Book>>

    suspend fun getSeenBooks(): List<Book>

    suspend fun markBookAsSeen(book: Book)

    suspend fun getBookDescription(bookId: String): String?

    fun getFavoriteBooks(): Flow<List<Book>>

    fun getReadingListBooks(): Flow<List<Book>>

    suspend fun toggleFavorite(book: Book)

    suspend fun toggleReadingList(book: Book)
}
