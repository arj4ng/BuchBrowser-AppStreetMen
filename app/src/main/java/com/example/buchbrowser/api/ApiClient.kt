package com.example.buchbrowser.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// app/src/main/java/com/example/buchbrowser/api/ApiClient.kt
// ApiClient
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
// TODO: ║    ApiClient.kt     ║
// TODO: ╚═════════════════════╝

private const val BASE_URL = "https://openlibrary.org/"

// TODO: ┏━ PROJEKTWOCHE | API
// TODO: ┗━ Verbindung zur Buch API

// stellt die Verbindung zur API her und schreibt Anfragen für Logcat mit
object ApiClient {

    // zeigt in Logcat genau, was bei der API Anfrage rausgeht und zurückkommt
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // hier hängt das Logging direkt am normalen Netzwerkclient dran
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // retrofit nimmt uns den ganzen HTTP und JSON Aufwand im Hintergrund ab
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // daraus entsteht am Ende unsere nutzbare API Schnittstelle für OpenLibrary
    val openLibraryApi: OpenLibraryApi = retrofit.create(OpenLibraryApi::class.java)
}
