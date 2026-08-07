package com.example.buchbrowser.repository

import com.example.buchbrowser.api.ApiClient
import com.example.buchbrowser.api.toBook
import com.example.buchbrowser.api.toDescription
import com.example.buchbrowser.data.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

class ApiBookRepository(
    private val random: Random = Random.Default,
) : BookRepository {

    private val randomQueries = listOf(
        "bestseller", "fantasy", "science fiction", "mystery", "romance", "history", "biography",
    )

    override suspend fun searchBooks(query: String): List<Book> {
        val response = ApiClient.openLibraryApi.searchBooks(query)
        return response.docs.map { it.toBook(isSeen = false) }
    }

    override suspend fun getBookDescription(bookId: String): String? {
        val response = ApiClient.openLibraryApi.getBookDetails(bookId)
        return response.toDescription()
    }

    override suspend fun getRandomBook(): Book? {
        randomQueries.shuffled(random).forEach { query ->
            runCatching { searchBooks(query) }.getOrNull()
                ?.filter { book -> book.title.isNotBlank() && book.id.isNotBlank() }
                ?.takeIf { it.isNotEmpty() }
                ?.let { books -> return books[random.nextInt(books.size)] }
        }
        return null
    }

    override fun getSeenBooksFlow(): Flow<List<Book>> = flowOf(emptyList())

    override suspend fun getSeenBooks(): List<Book> = emptyList()

    override suspend fun markBookAsSeen(book: Book) {}

    override fun getFavoriteBooks(): Flow<List<Book>> = flowOf(emptyList())

    override fun getReadingListBooks(): Flow<List<Book>> = flowOf(emptyList())

    override suspend fun toggleFavorite(book: Book) {}

    override suspend fun toggleReadingList(book: Book) {}
}
