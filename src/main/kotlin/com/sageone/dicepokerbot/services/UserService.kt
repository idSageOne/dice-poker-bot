package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.repositories.UserRepository
import com.sageone.dicepokerbot.roles.AlphaTester
import com.sageone.dicepokerbot.roles.BetaTester
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class UserService(
    val diceSetService: DiceSetService,
    val chatService: ChatService
) {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var alphaTester: AlphaTester

    @Autowired
    lateinit var betaTester: BetaTester

    fun createUser(message: Message): UserEntity {
        val telegramId = message.from.id
        return if (checkUserExists(telegramId)) {
            userRepository.findByTelegramId(telegramId)!!
        } else {
            var user = UserEntity()
            user.telegramId = telegramId
            user.telegramHandle = message.from.userName
            user.isBot = message.from.isBot
            user.isAlphaTester = alphaTester.id.contains(telegramId)
            user.isBetaTester = betaTester.id.contains(telegramId)
            user = userRepository.save(user)
            diceSetService.linkDiceSetToUser(user, "white", true)
            chatService.createChat(message)
            user
        }
    }

    fun checkUserExists(telegramId: Long): Boolean {
        return userRepository.countByTelegramId(telegramId) > 0
    }

}