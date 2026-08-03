package com.example.buchbrowser.repository

import com.example.buchbrowser.data.Book

// app/src/main/java/com/example/buchbrowser/repository/BookRepository.kt
// BookRepository
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
// TODO: ║  BookRepository.kt  ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | REPOSITORY
// TODO: ┗━ Gemeinsame Datenschnittstelle

// gemeinsame Stelle für alle Buchdaten
interface BookRepository {

    // gibt Suchtreffer zurück, egal ob API oder Mock
    suspend fun searchBooks(query: String): List<Book>

    // holt ein zufälliges Buch aus der aktuellen Quelle
    suspend fun getRandomBook(): Book?

    // holt alle lokal gemerkten Bücher
    suspend fun getSeenBooks(): List<Book>

    // merkt ein Buch lokal als gesehen
    suspend fun markBookAsSeen(book: Book)
}
