package ru.netology

class Message (
    var text: String = "",
    var read: Boolean,
    var income: Boolean
) {
    override fun toString(): String {
        return text
    }
}