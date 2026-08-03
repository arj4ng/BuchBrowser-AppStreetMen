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

// zentrale Room Datenbank für lokale Buchsachen
@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {

    // von hier kommt das DAO für alle Buch Abfragen
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var instance: BookDatabase? = null

        // baut die DB nur einmal und nutzt sie danach weiter
        fun getInstance(context: Context): BookDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    // nimmt absichtlich den app Context, nicht den von einer Activity
                    context.applicationContext,
                    BookDatabase::class.java,
                    // fester Name, damit immer dieselbe lokale DB genutzt wird
                    "buch_browser_database",
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
