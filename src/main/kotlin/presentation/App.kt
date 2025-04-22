package org.example.presentation

import org.example.presentation.MenuItem.EXIT
import org.example.presentation.MenuItem.entries

class App {
    fun start() {
        MenuItem.entries.forEachIndexed { index, option -> print("\n${index + 1}.\t${option.title}") }
        print("\nEnter your choice (1-${MenuItem.entries.size}) \u001B[33m*Enter ${MenuItem.entries.size} or anything else to exit*\u001B[0m: ")
        getMenuItemByIndex(readln().toIntOrNull() ?: -1).also { option ->
            if (option == EXIT) return
            option.execute()
            start()
        }
    }

    private fun getMenuItemByIndex(input: Int) = entries.getOrNull(input - 1) ?: EXIT
}
