package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.EAchievements
import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.services.AchievementService
import com.sageone.dicepokerbot.services.StatService
import com.sageone.dicepokerbot.services.UserService
import com.sageone.dicepokerbot.utils.createReply
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class AchievementsCommand(
    val userService: UserService,
    val statService: StatService,
    val achievementService: AchievementService
) : BotCommand(ECommands.ACHIEVEMENTS.text, ECommands.ACHIEVEMENTS.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val user = userService.createUser(message)
        val stats = statService.readOrCreateUserStats(user)
        val achievements = achievementService.readOrCreateUserAchievements(user)
        val achievementsMap = achievementService.achievementsToMap(achievements)
        val unlockedAchievementsMap = mutableMapOf<EAchievements, Boolean>()
        for (i in achievementsMap) {
            if (i.value) {
                unlockedAchievementsMap[i.key] = i.value
            }
        }
        val text = achievementService.generateAllAchievementsText(user, stats, unlockedAchievementsMap)
        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        absSender.execute(
            createReply(
                chatId = chatId,
                replyId = replyId,
                text = text
            )
        )
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}