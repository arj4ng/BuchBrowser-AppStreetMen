package com.example.buchbrowser.repository

import com.example.buchbrowser.api.OpenLibraryApi
import com.example.buchbrowser.api.toDescription
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

// verbindet API Suche mit Room
class BookRepositoryImpl(
    private val api: OpenLibraryApi,
    private val bookDao: BookDao,
    // random bleibt extra drin, damit Tests später leichter werden
    private val random: Random = Random.Default,
) : BookRepository {

    // hier kommen API Treffer und lokaler Status zusammen
    override suspend fun searchBooks(query: String): List<Book> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        // holt erst die IDs aus Room, damit Treffer gleich den richtigen Status haben
        val seenIds = bookDao.getSeenBooks().map { entity ->
            entity.id
        }.toSet()

        // leere API Treffer fliegen direkt raus
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

        // Zufall kommt gerade nur aus lokal gemerkten Büchern
        return seenBooks[random.nextInt(seenBooks.size)]
    }

    override suspend fun getSeenBooks(): List<Book> {
        // liest alle Room Einträge wieder als normale Bücher raus
        return bookDao.getSeenBooks().map { entity ->
            entity.toBook()
        }
    }

    override suspend fun markBookAsSeen(book: Book) {
        // setzt den Status vor dem Speichern nochmal sauber auf gesehen
        bookDao.insertSeenBook(book.copy(isSeen = true).toEntity())
    }

    override suspend fun getBookDescription(bookId: String): String? {
        // work ids kommen aus OpenLibrary oft mit führendem Slash, für den Endpoint reicht der Pfad ohne Slash
        val normalizedWorkKey = bookId.trim().removePrefix("/")
        if (normalizedWorkKey.isBlank()) {
            return null
        }

        return api.getBookDetails(workKey = normalizedWorkKey).toDescription()
    }
}
