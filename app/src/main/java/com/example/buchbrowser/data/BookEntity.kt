package com.example.buchbrowser.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// app/src/main/java/com/example/buchbrowser/data/BookEntity.kt
// BookEntity
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
// TODO: ║    BookEntity.kt    ║
// TODO: ╚═════════════════════╝

private const val LIST_SEPARATOR = " | "

// TODO: ┏━ PROJEKTWOCHE | ROOM
// TODO: ┗━ Gesehene Bücher speichern

// room merkt sich hier bereits gesehene Bücher
@Entity(tableName = "seen_books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authorNamesText: String,
    val coverUrl: String?,
    val firstPublishYear: Int?,
    val languageCodesText: String,
)

// wandelt Roomdaten zurück ins Appmodell
fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        authorNames = authorNamesText.decodeList(),
        coverUrl = coverUrl,
        firstPublishYear = firstPublishYear,
        languageCodes = languageCodesText.decodeList(),
        isSeen = true,
    )
}

// macht aus Appdaten einen Room Eintrag
fun Book.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        authorNamesText = authorNames.encodeList(),
        coverUrl = coverUrl,
        firstPublishYear = firstPublishYear,
        languageCodesText = languageCodes.encodeList(),
    )
}

private fun List<String>.encodeList(): String {
    // room speichert Listen hier erstmal simpel als einen Text
    return joinToString(separator = LIST_SEPARATOR)
}

private fun String.decodeList(): List<String> {
    if (isBlank()) {
        return emptyList()
    }

    // beim Lesen wird derselbe Trenner wieder sauber zurück in eine Liste gebaut
    return split(LIST_SEPARATOR).map { item ->
        item.trim()
    }
}
