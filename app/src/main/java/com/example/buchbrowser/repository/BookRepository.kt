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

// gemeinsame Schnittstelle für alle Buchdaten
interface BookRepository {

    // gibt Suchtreffer zurück, egal ob Daten später aus API oder Mock kommen
    suspend fun searchBooks(query: String): List<Book>

    // liefert ein zufälliges Buch aus der gerade verfügbaren Datenquelle
    suspend fun getRandomBook(): Book?

    // holt alle Bücher, die lokal schon als gesehen gespeichert wurden
    suspend fun getSeenBooks(): List<Book>

    // merkt ein Buch lokal, damit der Status später wieder da ist
    suspend fun markBookAsSeen(book: Book)
}
