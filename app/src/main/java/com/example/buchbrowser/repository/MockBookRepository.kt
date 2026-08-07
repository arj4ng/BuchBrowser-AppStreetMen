package com.example.buchbrowser.repository

import com.example.buchbrowser.data.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class MockBookRepository(
    private val random: Random = Random.Default,
) : BookRepository {

    private val sampleBooks = listOf(
        Book(
            id = "/works/OL45883W",
            title = "Der kleine Hobbit",
            authorNames = listOf("J. R. R. Tolkien"),
            coverUrl = "https://openlibrary.org/search.json?q=the+lord+of+the+rings",
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

    private val seenBookIds = linkedSetOf<String>()
    private val favoriteBookIds = linkedSetOf<String>()
    private val readingListBookIds = linkedSetOf<String>()

    override suspend fun searchBooks(query: String): List<Book> {
        val normalizedQuery = query.trim().lowercase()
        val filtered = if (normalizedQuery.isBlank()) {
            sampleBooks
        } else {
            sampleBooks.filter { book ->
                book.title.lowercase().contains(normalizedQuery) ||
                        book.authorNames.any { author ->
                            author.lowercase().contains(normalizedQuery)
                        }
            }
        }
        return filtered.map { book ->
            book.copy(
                isSeen = seenBookIds.contains(book.id),
                isFavorite = favoriteBookIds.contains(book.id),
                isInReadingList = readingListBookIds.contains(book.id)
            )
        }
    }

    override suspend fun getRandomBook(): Book? {
        if (sampleBooks.isEmpty()) return null
        val randomBook = sampleBooks[random.nextInt(sampleBooks.size)]
        return randomBook.copy(
            isSeen = seenBookIds.contains(randomBook.id),
            isFavorite = favoriteBookIds.contains(randomBook.id),
            isInReadingList = readingListBookIds.contains(randomBook.id)
        )
    }

    override fun getSeenBooksFlow(): Flow<List<Book>> {
        return flowOf(sampleBooks.filter { seenBookIds.contains(it.id) }.map {
            it.copy(
                isSeen = true,
                isFavorite = favoriteBookIds.contains(it.id),
                isInReadingList = readingListBookIds.contains(it.id)
            )
        })
    }

    override suspend fun getSeenBooks(): List<Book> {
        return sampleBooks.filter { seenBookIds.contains(it.id) }.map {
            it.copy(
                isSeen = true,
                isFavorite = favoriteBookIds.contains(it.id),
                isInReadingList = readingListBookIds.contains(it.id)
            )
        }
    }

    override suspend fun markBookAsSeen(book: Book) {
        seenBookIds.add(book.id)
    }

    override suspend fun getBookDescription(bookId: String): String? {
        return when (bookId) {
            "/works/OL45883W" -> "Bilbo stolpert eher zufällig in ein großes Abenteuer..."
            "/works/OL27448W" -> "Elizabeth Bennet gerät zwischen Stolz, Vorurteile..."
            "/works/OL82563W" -> "Momo hört zu wie kaum jemand sonst..."
            else -> null
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return flowOf(sampleBooks.filter { favoriteBookIds.contains(it.id) }.map {
            it.copy(isFavorite = true, isSeen = seenBookIds.contains(it.id), isInReadingList = readingListBookIds.contains(it.id))
        })
    }

    override fun getReadingListBooks(): Flow<List<Book>> {
        return flowOf(sampleBooks.filter { readingListBookIds.contains(it.id) }.map {
            it.copy(isInReadingList = true, isSeen = seenBookIds.contains(it.id), isFavorite = favoriteBookIds.contains(it.id))
        })
    }

    override suspend fun toggleFavorite(book: Book) {
        if (favoriteBookIds.contains(book.id)) {
            favoriteBookIds.remove(book.id)
        } else {
            favoriteBookIds.add(book.id)
            seenBookIds.add(book.id)
        }
    }

    override suspend fun toggleReadingList(book: Book) {
        if (readingListBookIds.contains(book.id)) {
            readingListBookIds.remove(book.id)
        } else {
            readingListBookIds.add(book.id)
            seenBookIds.add(book.id)
        }
    }
}
