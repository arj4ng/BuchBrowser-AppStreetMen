package com.example.buchbrowser.repository

import com.example.buchbrowser.api.OpenLibraryApi
import com.example.buchbrowser.api.toBook
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.data.BookDao
import com.example.buchbrowser.data.toBook
import com.example.buchbrowser.data.toEntity
import kotlin.random.Random

// app/src/main/java/com/example/buchbrowser/repository/BookRepositoryInpl.kt
// BookRepositoryInpl
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
// TODO: ║ BookRepositoryInpl  ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | REPOSITORY
// TODO: ┗━ API und Room verbinden

// verbindet API Suche mit lokalem Room Speicher
class BookRepositoryImpl(
    private val api: OpenLibraryApi,
    private val bookDao: BookDao,
    // random bleibt injizierbar, damit Zufall im Test später kontrollierbar bleibt
    private val random: Random = Random.Default,
) : BookRepository {

    // hier laufen API Suche und lokaler Gesehen Status einmal zusammen
    override suspend fun searchBooks(query: String): List<Book> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        // holt zuerst alle bekannten IDs aus Room, damit Suchtreffer sofort den richtigen Status tragen
        val seenIds = bookDao.getSeenBooks().map { entity ->
            entity.id
        }.toSet()

        // leere API Treffer fliegen hier direkt raus, damit später in der UI nichts Halbkaputtes landet
        return api.searchBooks(query = normalizedQuery).docs.mapNotNull { bookDto ->
            val bookId = bookDto.key.orEmpty()
            val title = bookDto.title.orEmpty()

            if (bookId.isBlank() || title.isBlank()) {
                null
            } else {
                bookDto.toBook(isSeen = seenIds.contains(bookId))
            }
        }
    }

    override suspend fun getRandomBook(): Book? {
        val seenBooks = getSeenBooks()
        if (seenBooks.isEmpty()) {
            return null
        }

        // für den Moment kommt Zufall nur aus bereits lokal gemerkten Büchern
        return seenBooks[random.nextInt(seenBooks.size)]
    }

    override suspend fun getSeenBooks(): List<Book> {
        // liest alle Room Einträge und macht daraus wieder normale Buchmodelle
        return bookDao.getSeenBooks().map { entity ->
            entity.toBook()
        }
    }

    override suspend fun markBookAsSeen(book: Book) {
        // setzt den Status bewusst vor dem Speichern, damit Room immer den aktuellen Stand bekommt
        bookDao.insertSeenBook(book.copy(isSeen = true).toEntity())
    }
}
