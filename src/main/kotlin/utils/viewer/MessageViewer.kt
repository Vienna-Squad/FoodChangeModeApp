package org.example.utils.viewer

class MessageViewer : ItemDetailsViewer<String> {
    override fun viewDetails(item: String) {
        println(item)
    }
}