package org.example.utils.viewer

class FoodExceptionViewer : ExceptionViewer {
    override fun viewExceptionMessage(exception: Exception) {
        println("\u001B[34m${exception.message}\u001B[0m")
    }
}