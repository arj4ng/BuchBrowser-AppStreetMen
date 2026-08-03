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

// alle Room Sachen für gesehene Bücher
@Dao
interface BookDao {

    // speichert ein Buch, gleiche ID wird ersetzt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBook(book: BookEntity)

    // nützlich, wenn später mehrere Bücher auf einmal rein sollen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeenBooks(books: List<BookEntity>)

    // holt alle gemerkten Bücher alphabetisch raus
    @Query("SELECT * FROM seen_books ORDER BY title ASC")
    suspend fun getSeenBooks(): List<BookEntity>

    // kurzer Check, ob ein Buch schon als gesehen drinliegt
    @Query("SELECT EXISTS(SELECT 1 FROM seen_books WHERE id = :bookId)")
    suspend fun isBookSeen(bookId: String): Boolean

    // leert die Tabelle, eher für Tests oder Reset
    @Query("DELETE FROM seen_books")
    suspend fun clearSeenBooks()
}
