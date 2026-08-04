package com.example.buchbrowser.navigation

import kotlinx.serialization.Serializable

/**
 * Repräsentiert alle Navigationsziele (Routen) innerhalb der Anwendung.
 *
 * Jede Route wird als typsicheres, serialisierbares Objekt definiert, um Fehler
 * bei der Argumentenübergabe und Tippfehler in Pfad-Strings zur Kompilierzeit zu vermeiden.
 *
 * @see Route.Home
 * @see Route.Search
 */
sealed interface Route {
    /**
     * Startbildschirm der Anwendung.
     * Zeigt die Hauptübersicht / das Dashboard an.
     */
    @Serializable
    data object Home: Route
    /**
     * Suchbildschirm der Anwendung.
     * Ermöglicht das Suchen und Filtern von Inhalten.
     */
    @Serializable
    data object Search: Route

    /**
     * Detailscreen für ein einzelnes Buch.
     * Trägt die Basisdaten direkt über Navigation mit.
     */
    @Serializable
    data class Details(
        val id: String,
        val title: String,
        val author: String,
        val coverUrl: String?,
        val firstPublishYear: Int?,
    ) : Route
}
