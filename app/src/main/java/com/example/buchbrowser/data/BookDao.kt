package com.example.buchbrowser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// app/src/main/java/com/example/buchbrowser/data/BookDao.kt
// BookDao
//
// Created by Arjang Khademi on 03.08.2026
/*
  ╔════════════════════════════════════════════════════════╗
  ║  █████╗ ██████╗      ██╗ ██╗  ██╗ ███╗   ██╗  ██████╗  ║
  ║ ██╔══██╗██╔══██╗     ██║ ██║  ██║ ████╗  ██║ ██╔════╝  ║
  ║ ███████║██████╔╝     ██║ ███████║ ██╔██╗ ██║ ██║  ███╗ ║
  ║ ██╔══██║██╔══██╗██   ██║ ╚════██║ ██║╚██╗██║ ██║   ██║ ║
  ║ ██║  ██║██║  ██║╚█████╔╝      ██║ ██║ ╚████║ ╚██████╔╝ ║
  ║ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚════╝       ╚═╝ ╚═╝  ╚═══╝  ╚═════╝  ║
  ╚═════════════════════════════════════════ [ DEV TAG ] ══╝
*/

// TODO: ╔══ [ BuchBrowser ] ══╗
// TODO: ║      BookDao.kt     ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | ROOM
// TODO: ┗━ Zugriff auf gesehene Bücher

// alle Room Zugriffe für gesehene Bücher
@Dao
interface BookDao {

    // speichert genau ein Buch und überschreibt alten Stand bei gleicher ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBook(book: BookEntity)

    // praktisch, wenn später mal mehrere Bücher auf einmal lokal landen sollen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBooks(books: List<BookEntity>)

    // gibt alle gemerkten Bücher alphabetisch zurück, damit Listen stabil bleiben
    @Query("SELECT * FROM seen_books ORDER BY title ASC")
    suspend fun getSeenBooks(): List<BookEntity>

    // schneller Check, ob ein Buch lokal schon als gesehen markiert wurde
    @Query("SELECT EXISTS(SELECT 1 FROM seen_books WHERE id = :bookId)")
    suspend fun isBookSeen(bookId: String): Boolean

    // nur fürs Leeren der Tabelle, z. B. bei Tests oder Reset
    @Query("DELETE FROM seen_books")
    suspend fun clearSeenBooks()
}
