package com.example.buchbrowser.data

import com.example.buchbrowser.R

fun Book.getOpenSound(): Int? {
    return when {

        title.contains("Harry Potter", true) ->
            R.raw.harry1

        title.contains("Lord of the", true) ||
                title.contains("The Hobbit", true) ||
                title.contains("Hobbit", true) ||
                title.contains("Der Hobbit", true) ->
            R.raw.herr1

        title.contains("Panem", true) ||
                title.contains("Hunger Games", true) ->

            R.raw.panem1

        title.contains("Game of Thrones", true) ||
                title.contains("Song of Ice and Fire", true) ->
            R.raw.game1

        else -> null
    }
}