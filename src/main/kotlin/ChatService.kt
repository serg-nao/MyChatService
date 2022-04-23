package ru.netology

fun main() {
    val service = ChatService()
    var chat = service.addMessage(1, 0, "hello")
    println(chat)
    chat = service.addMessage(2, 0, "hi")
    println(chat)
    chat = service.addMessage(3, 0, "How are you?")
    println(chat)
    chat = service.addMessage(1, 0, "Привет")
    println(chat)
    chat = service.addMessage(2, 0, "fine")
    println(chat)
    println(service.getUnreadChatsCount(0))

}

class ChatService{
    val chats = hashMapOf<String, Chat>()
    private var chatId = ""
    private val myUserId = 0

    private fun makeKey(userId1: Int, userId2: Int): String {
        val min = minOf(userId1, userId2)
        val max = maxOf(userId1, userId2)
        return "$min.$max"
    }

    fun getChats(userId: Int) : List<Chat> {
        val keyBegin = "$userId."
        val keyEnd = ".$userId"
        return chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList()
    }

    fun getChat(userId: Int, chatId: String) : Chat? {
        return getChats(userId).find { it.id == chatId }
    }

    fun getMessagesByChatId(userId: Int, chatId: String) : List<Message>? {
        val chat = getChat(userId, chatId)
        chat?.messages?.filter { !it.read }?.forEach { it.read = true }
        return chat?.messages
    }

    fun getMessagesByMessageId(userId: Int, chatId: String, id: Int) : List<Message>? {
        val chat = getChat(userId, chatId)
        val count = chat?.messages?.size?.minus(id)
        chat?.messages?.filter { !it.read }?.forEach { it.read = true }
        return count?.let { chat.messages.takeLast(it) }
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return getChats(userId).count { it.count > 0 }
    }

    fun addMessage(senderId: Int, recipientId: Int, text: String) : Chat {
        val key = makeKey(senderId, recipientId)
        if (chats[key] == null) {
            chatId = key
        }
        return chats.getOrPut(key) {Chat(id = chatId)}.apply { messages.add(Message(
            text = text,
            read = senderId == myUserId,
            income = senderId != myUserId
        )) }
    }

    fun deleteChat(chatId: String) : Boolean {
        return if (chats[chatId] == null) false else {
            chats.remove(chatId)
            true
        }
    }

    fun deleteMessage(userId: Int, chatId: String, id: Int) : Boolean {
        if (getChat(userId, chatId) != null) {
            val message = getChat(userId, chatId)!!.messages[id]
            getChat(userId, chatId)!!.messages.remove(message)
            if (getChat(userId, chatId)!!.messages.isEmpty()) {
                deleteChat(chatId)
            }
            return true
        }
        return false
    }

    fun editMessage(userId: Int, chatId: String, id: Int, text: String) : Message {
        getChat(userId, chatId)!!.messages[id] = Message(
            text = text,
            read = getChat(userId, chatId)!!.messages[id].read,
            income = getChat(userId, chatId)!!.messages[id].income
        )
        return getChat(userId, chatId)!!.messages[id]
    }
}