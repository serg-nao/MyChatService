import org.junit.Assert.*
import org.junit.Test
import ru.netology.Chat
import ru.netology.ChatService
import ru.netology.Message

internal class ChatServiceTest {
    private val service = ChatService()
    private val chats = hashMapOf<String, Chat>(
        "0.1" to Chat("0.1", mutableListOf(
            Message("text1", false, true),
            Message("text2", false, true)
        )),
        "0.2" to Chat("0.2", mutableListOf(
            Message("text3", false, true),
            Message("text4", false, true)
        )),
        "0.3" to Chat("0.3", mutableListOf(
            Message("text5", false, true)
        ))
    )
    var chatId = ""
    val myUserId = 0



    @Test
    fun getChats() {
        //arrange
        val keyBegin = "0."
        val keyEnd = ".0"

        //act
        val list = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList()
        val result = list.size

        //assert
        assertEquals(3, result)

    }

    @Test
    fun getChat() {
        //arrange
        val keyBegin = "1."
        val keyEnd = ".1"
        val userId = 1
        chatId = "0.1"

        //act
        val chatResult = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }
        val result = chatResult!!.messages.last().text

        //assert
        assertEquals("text2", result)

    }

    @Test
    fun getMessagesByChatId() {
        //arrange
        val keyBegin = "1."
        val keyEnd = ".1"
        chatId = "0.1"

        //act
        val chatResult = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }
        val result: Int = chatResult!!.messages.size

        //assert
        assertEquals(2, result)
    }

    @Test
    fun getMessagesByMessageId() {
        //arrange
        val keyBegin = "1."
        val keyEnd = ".1"
        chatId = "0.1"
        val id = 1

        //act
        val chatResult = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }
        val count = chatResult!!.messages.size.minus(id)

        //assert
        assertEquals(1, count)
    }

    @Test
    fun getUnreadChatsCount() {
        //arrange
        val keyBegin = "0."
        val keyEnd = ".0"

        //act
        val result = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().count { it.count > 0 }

        //assert
        assertEquals(3, result)

    }

    @Test
    fun addMessage_toExistChat() {
        //arrange
        val senderId = 0
        val text = "this text"
        val key = "0.1"

        //act
        chats.getOrPut(key) { Chat(id = key)}.apply { messages.add(Message(
            text = text,
            read = senderId == myUserId,
            income = senderId != myUserId
        ))}
        val chatResult = chats.filterKeys { it.contains(key) }.values.toList().find { it.id == key }
        val result = chatResult!!.messages.last().text

        //assert
        assertEquals(text, result)
    }

    @Test
    fun addMessage_toNewChat() {
        //arrange
        val senderId = 0
        val text = "this text"
        val key = "0.4"

        //act
        chats.getOrPut(key) { Chat(id = key)}.apply { messages.add(Message(
            text = text,
            read = senderId == myUserId,
            income = senderId != myUserId
        ))}
        val chatResult = chats.filterKeys { it.contains(key) }.values.toList().find { it.id == key }
        val result = chatResult!!.messages.last().text

        //assert
        assertEquals(text, result)
    }

    @Test
    fun deleteChat_true() {
        //arrange
        val chatId = "0.3"
        val result: Boolean

        //act
        if (chats[chatId] == null) {
            result = false
        } else {
            chats.remove(chatId)
            result = chats.size == 2
        }

        //assert
        assertTrue(result)
    }

    @Test
    fun deleteChat_false() {
        //arrange
        val chatId = "1.3"
        val result: Boolean

        //act
        if (chats[chatId] == null) {
            result = false
        } else {
            chats.remove(chatId)
            result = true
        }

        //assert
        assertFalse(result)
    }

    @Test
    fun deleteMessage_true() {
        //arrange
        val chatId = "0.2"
        val keyBegin = "0."
        val keyEnd = ".0"
        val id = 1

        //act
        if (chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId } != null) {
            val message = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id]
            chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages.remove(message)
        }
        val result = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages.size

        //assert
        assertEquals(1, result)
    }

    @Test
    fun deleteMessage_deleteChat_true() {
        //arrange
        val chatId = "0.3"
        val keyBegin = "0."
        val keyEnd = ".0"
        val id = 0

        //act
        if (chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId } != null) {
            val message = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id]
            chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages.remove(message)
            if (chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages.isEmpty()) {
                chats.remove(chatId)
            }
        }
        val result = chats.size == 2

        //assert
        assertTrue(result)
    }

    @Test
    fun deleteMessage_false() {
        //arrange
        val chatId = "0.4"
        val keyBegin = "0."
        val keyEnd = ".0"
        val id = 1
        var result: Boolean

        //act
        if (chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId } != null) {
            val message = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id]
            chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages.remove(message)
            result = true
        }
        result = false

        //assert
        assertFalse(result)
    }

    @Test
    fun editMessage() {
        //arrange
        val chatId = "0.2"
        val keyBegin = "0."
        val keyEnd = ".0"
        val userId = 0
        val id = 0
        val text = "hello"

        //act
        chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id] = Message(
            text = text,
            read = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id].read,
            income = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id].income
        )
        val result = chats.filterKeys { it.contains(keyBegin) || it.contains(keyEnd) }.values.toList().find { it.id == chatId }!!.messages[id].text

        //assert
        assertEquals(text, result)
    }
}