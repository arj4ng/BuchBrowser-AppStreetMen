package com.example.buchbrowser.repository

import com.example.buchbrowser.data.Book
import kotlin.random.Random

// app/src/main/java/com/example/buchbrowser/repository/MockBookRepository.kt
// MockBookRepository
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
// TODO: ║ MockBookRepository  ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | MOCKDATEN
// TODO: ┗━ Beispieldaten für Team

// liefert feste Beispieldaten für Tests und UI
class MockBookRepository(
    // auch hier bleibt Zufall austauschbar, falls man später gezielt testen will
    private val random: Random = Random.Default,
) : BookRepository {

    // kleine feste Liste, damit Team und UI ohne echte API schon arbeiten können
    private val sampleBooks = listOf(
        Book(
            id = "/works/OL45883W",
            title = "Der kleine Hobbit",
            authorNames = listOf("J. R. R. Tolkien"),
            coverUrl = "https://covers.openlibrary.org/b/id/14625727-M.jpg",
            firstPublishYear = 1937,
            languageCodes = listOf("ger"),
            isSeen = false,
        ),
        Book(
            id = "/works/OL27448W",
            title = "Stolz und Vorurteil",
            authorNames = listOf("Jane Austen"),
            coverUrl = "https://covers.openlibrary.org/b/id/8231856-M.jpg",
            firstPublishYear = 1813,
            languageCodes = listOf("ger", "eng"),
            isSeen = false,
        ),
        Book(
            id = "/works/OL82563W",
            title = "Momo",
            authorNames = listOf("Michael Ende"),
            coverUrl = "https://covers.openlibrary.org/b/id/10749658-M.jpg",
            firstPublishYear = 1973,
            languageCodes = listOf("ger"),
            isSeen = false,
        ),
    )

    // merkt sich nur die IDs, weil für den Gesehen Status mehr nicht nötig ist
    private val seenBookIds = linkedSetOf<String>()

    // sucht nur in den Mockdaten und verhält sich nach außen wie das echte Repository
    override suspend fun searchBooks(query: String): List<Book> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        return sampleBooks.filter { book ->
            val titleMatches = book.title.lowercase().contains(normalizedQuery)
            val authorMatches = book.authorNames.any { authorName ->
                authorName.lowercase().contains(normalizedQuery)
            }

            titleMatches || authorMatches
        }.map { book ->
            // setzt den lokalen Gesehen Status direkt auf das Ergebnis, damit UI sofort passt
            book.copy(isSeen = seenBookIds.contains(book.id))
        }
    }

    override suspend fun getRandomBook(): Book? {
        if (sampleBooks.isEmpty()) {
            return null
        }

        val randomBook = sampleBooks[random.nextInt(sampleBooks.size)]

        return randomBook.copy(isSeen = seenBookIds.contains(randomBook.id))
    }

    override suspend fun getSeenBooks(): List<Book> {
        // gibt nur die Bücher zurück, deren ID vorher lokal gemerkt wurde
        return sampleBooks.filter { book ->
            seenBookIds.contains(book.id)
        }.map { book ->
            book.copy(isSeen = true)
        }
    }

    override suspend fun markBookAsSeen(book: Book) {
        // set verhindert doppelte Einträge automatisch
        seenBookIds.add(book.id)
    }
}
