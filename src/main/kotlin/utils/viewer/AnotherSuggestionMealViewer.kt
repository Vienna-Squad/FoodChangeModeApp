package org.example.utils.viewer

class AnotherSuggestionMealViewer(): ItemDetailsViewer<String> {
    override fun viewDetails(item: String) {
        print(item)
    }

}