package org.example.utils.viewer

interface ItemsViewer<T> {
    fun viewItems(items: List<T>)
}