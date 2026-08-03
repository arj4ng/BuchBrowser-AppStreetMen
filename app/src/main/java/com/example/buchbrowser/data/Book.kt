package com.example.buchbrowser.data

// app/src/main/java/com/example/buchbrowser/data/Book.kt
// Book
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
// TODO: ║       Book.kt       ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DATENMODELL
// TODO: ┗━ Gemeinsames Buchmodell

// gemeinsames Buchmodell für App und Datenquellen
data class Book(
    val id: String,
    val title: String,
    val authorNames: List<String>,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val languageCodes: List<String>,
    val isSeen: Boolean,
)
