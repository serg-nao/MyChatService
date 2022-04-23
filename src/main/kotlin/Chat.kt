package ru.netology

class Chat (
    val id: String,
    val messages: MutableList<Message> = mutableListOf()
) {

    val count: Int
        get() = messages.count{ !it.read }

    override fun toString(): String {

        return if (count > 0) {
            "Чат $id (непрочитанных сообщений $count): " + messages.last().toString()
        } else {
            "Чат $id: " + messages.last().toString()
        }
    }
}