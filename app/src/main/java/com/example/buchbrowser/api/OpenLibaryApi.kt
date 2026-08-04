package com.example.buchbrowser.api

import com.example.buchbrowser.data.Book
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// app/src/main/java/com/example/buchbrowser/api/OpenLibaryApi.kt
// OpenLibaryApi
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
// TODO: ║  OpenLibaryApi.kt   ║
// TODO: ╚═════════════════════╝

private const val COVER_URL_TEMPLATE = "https://covers.openlibrary.org/b/id/%s-M.jpg"

// TODO: ┏━ PROJEKTWOCHE | API
// TODO: ┗━ Bücher bei OpenLibrary suchen

// hier stehen die Anfragen an OpenLibrary
interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
    ): OpenLibrarySearchResponse
}

// so sieht eine Buchsuche von der API aus
data class OpenLibrarySearchResponse(
    @SerializedName("docs")
    val docs: List<OpenLibraryBookDto>,
)

// rohe Buchdaten aus der API
data class OpenLibraryBookDto(
    @SerializedName("key")
    val key: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("author_name")
    val authorNames: List<String>?,
    @SerializedName("cover_i")
    val coverId: Int?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("language")
    val languageCodes: List<String>?,
)

// macht aus API Zeug unser normales Buchmodell
fun OpenLibraryBookDto.toBook(isSeen: Boolean): Book {
    return Book(
        id = key.orEmpty(),
        title = title.orEmpty(),
        authorNames = authorNames.orEmpty(),
        // OpenLibrary gibt nur die Cover ID, die URL bauen wir selbst
        coverUrl = coverId?.let { value ->
            COVER_URL_TEMPLATE.format(value)
        },
        firstPublishYear = firstPublishYear,
        languageCodes = languageCodes.orEmpty(),
        isSeen = isSeen,
    )
}
