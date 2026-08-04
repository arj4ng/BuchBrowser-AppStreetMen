package com.example.buchbrowser.repository


import com.example.buchbrowser.api.ApiClient
import com.example.buchbrowser.api.toBook
import com.example.buchbrowser.api.toDescription
import com.example.buchbrowser.data.Book

class ApiBookRepository : BookRepository {

    override suspend fun searchBooks(query: String): List<Book> {

        val response = ApiClient.openLibraryApi.searchBooks(query)

        return response.docs.map {
            it.toBook(isSeen = false)
        }
    }

    override suspend fun getBookDescription(bookId: String): String? {

        val response = ApiClient.openLibraryApi.getBookDetails(bookId)

        return response.toDescription()
    }

    override suspend fun getRandomBook(): Book? {

        return searchBooks("a").randomOrNull()
    }

    override suspend fun getSeenBooks(): List<Book> {

        // Kommt später aus Room
        return emptyList()
    }

    override suspend fun markBookAsSeen(book: Book) {

        // Kommt später mit Room
    }
}