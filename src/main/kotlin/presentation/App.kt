package org.example.presentation

import org.example.utils.MenuItem
import org.example.utils.MenuItem.EXIT
import org.example.utils.MenuItem.entries

class App {
    fun start() {
        displayMenu()
        toMenuItem(readln().toIntOrNull() ?: -1).also { menuItem ->
            if (menuItem == EXIT) return
            menuItem.uiController.execute()
            println()
            start()
        }
    }

    private fun displayMenu() {
        MenuItem.entries.forEachIndexed { index, option ->
            when (index) {
                0 -> printCategoryHeader(" General Meal Discovery")
                6 -> printCategoryHeader(" Dietary & Nutrition")
                11 -> printCategoryHeader(" Ingredient Specials")
                13 -> printCategoryHeader(" Fun Activities")
                16 -> printCategoryHeader(" System")
            }
            val number = (index + 1).toString().padEnd(2)
            println("  \u001B[32m$number.\u001B[0m ${option.title}")
        }
        println("\n\u001B[33mEnter your choice (1-${MenuItem.entries.size}): \u001B[0m")
        print("\u001B[33m*Enter 16 or Anything Else to Exit*\u001B[0m:")
    }

    private fun printCategoryHeader(title: String) {
        println("\n\u001B[1;33m$title\u001B[0m")
        println("\u001B[37m${"-".repeat(title.length)}\u001B[0m")
    }

    private fun toMenuItem(input: Int) = entries.getOrNull(input - 1) ?: EXIT
}
