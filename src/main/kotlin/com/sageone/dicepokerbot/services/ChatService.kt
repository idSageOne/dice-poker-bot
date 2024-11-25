package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.ChatEntity
import com.sageone.dicepokerbot.database.jpa.repositories.ChatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ChatService {

    @Autowired
    lateinit var chatRepository: ChatRepository

    fun createChat(message: Message): ChatEntity {
        val telegramId = message.chat.id
        return if (checkChatExists(telegramId)) {
            chatRepository.findByTelegramId(telegramId)!!
        } else {
            val chat = ChatEntity()
            chat.telegramId = telegramId
            chat.isUserChat = message.chat.isUserChat
            chat.isGroupChat = message.chat.isGroupChat
            chatRepository.save(chat)
        }
    }

    fun checkChatExists(telegramId: Long): Boolean {
        return chatRepository.countByTelegramId(telegramId) > 0
    }

}