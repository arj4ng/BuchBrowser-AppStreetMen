package com.example.buchbrowser.repository

import com.example.buchbrowser.api.OpenLibraryApi
import com.example.buchbrowser.api.toDescription
import com.example.buchbrowser.api.toBook
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.data.BookDao
import com.example.buchbrowser.data.toBook
import com.example.buchbrowser.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class BookRepositoryImpl(
    private val api: OpenLibraryApi,
    private val bookDao: BookDao,
    private val random: Random = Random.Default,
) : BookRepository {

    override suspend fun searchBooks(query: String): List<Book> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return emptyList()
        }

        val localBooks = bookDao.getSeenBooks().associateBy { it.id }

        return api.searchBooks(query = normalizedQuery).docs.mapNotNull { bookDto ->
            val bookId = bookDto.key.orEmpty()
            val title = bookDto.title.orEmpty()

            if (bookId.isBlank() || title.isBlank()) {
                null
            } else {
                val local = localBooks[bookId]
                bookDto.toBook(isSeen = local != null).copy(
                    isFavorite = local?.isFavorite ?: false,
                    isInReadingList = local?.isInReadingList ?: false
                )
            }
        }
    }

    override suspend fun getRandomBook(): Book? {
        val seenBooks = getSeenBooks()
        if (seenBooks.isEmpty()) {
            return null
        }
        return seenBooks[random.nextInt(seenBooks.size)]
    }

    override fun getSeenBooksFlow(): Flow<List<Book>> {
        return bookDao.getSeenBooksFlow().map { entities ->
            entities.map { it.toBook() }
        }
    }

    override suspend fun getSeenBooks(): List<Book> {
        return bookDao.getSeenBooks().map { it.toBook() }
    }

    override suspend fun markBookAsSeen(book: Book) {
        bookDao.insertSeenBook(book.copy(isSeen = true).toEntity())
    }

    override suspend fun getBookDescription(bookId: String): String? {
        val normalizedWorkKey = bookId.trim().removePrefix("/")
        if (normalizedWorkKey.isBlank()) {
            return null
        }
        return api.getBookDetails(workKey = normalizedWorkKey).toDescription()
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return bookDao.getFavoriteBooks().map { entities ->
            entities.map { it.toBook() }
        }
    }

    override fun getReadingListBooks(): Flow<List<Book>> {
        return bookDao.getReadingListBooks().map { entities ->
            entities.map { it.toBook() }
        }
    }

    override suspend fun toggleFavorite(book: Book) {
        val existing = bookDao.getBookById(book.id)
        if (existing != null) {
            bookDao.insertSeenBook(existing.copy(isFavorite = !existing.isFavorite))
        } else {
            bookDao.insertSeenBook(book.copy(isFavorite = true, isSeen = true).toEntity())
        }
    }

    override suspend fun toggleReadingList(book: Book) {
        val existing = bookDao.getBookById(book.id)
        if (existing != null) {
            bookDao.insertSeenBook(existing.copy(isInReadingList = !existing.isInReadingList))
        } else {
            bookDao.insertSeenBook(book.copy(isInReadingList = true, isSeen = true).toEntity())
        }
    }
}
