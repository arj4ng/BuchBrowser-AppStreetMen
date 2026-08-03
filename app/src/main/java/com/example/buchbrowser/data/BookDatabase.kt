package com.example.buchbrowser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// app/src/main/java/com/example/buchbrowser/data/BookDatabase.kt
// BookDatabase
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
// TODO: ║   BookDatabase.kt   ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | ROOM
// TODO: ┗━ Datenbank für Cache

// zentrale Room Datenbank für lokale Buchdaten
@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {

    // hier holt sich der Rest der App den Zugriff auf unsere Buch Abfragen
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var instance: BookDatabase? = null

        // sorgt dafür, dass die Datenbank nur einmal gebaut und danach wiederverwendet wird
        fun getInstance(context: Context): BookDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    // applicationContext verhindert, dass aus Versehen ein Activity Context hängen bleibt
                    context.applicationContext,
                    BookDatabase::class.java,
                    // fester Name = dieselbe lokale Datenbank bei jedem App Start
                    "buch_browser_database",
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
