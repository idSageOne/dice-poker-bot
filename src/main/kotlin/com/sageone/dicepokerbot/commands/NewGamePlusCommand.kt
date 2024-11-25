package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.services.AchievementService
import com.sageone.dicepokerbot.services.StatService
import com.sageone.dicepokerbot.services.UserService
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.code
import com.sageone.dicepokerbot.utils.createReply
import com.sageone.dicepokerbot.utils.emoji
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class NewGamePlusCommand(
    val userService: UserService,
    val statService: StatService,
    val achievementService: AchievementService
) : BotCommand(ECommands.NEW_GAME_PLUS.text, ECommands.NEW_GAME_PLUS.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        // Инициализировать
        val user = userService.createUser(message)
        val stats = statService.readOrCreateUserStats(user)

        var result = emoji(10068) + bold("Новая игра плюс") +
                " - это дополнительный режим, в котором вы сможете заработать еще больше баллов " +
                "за каждое комбо и продолжить прогрессию достижений!" +
                "\n${emoji(10071)}" + "При старте новой игры часть статистики обнулится, " +
                "но игровая прогрессия продолжится. " +
                "\n${emoji(10071)}" + "Кроме того, за каждый старт новой игры вы получите " +
                bold("токены") + " - специальную валюту для секретного магазина!"

        val allAchievements = achievementService.readAchievements()
        val ownedAchievements = achievementService.readUserAchievements(user, stats.newGamePlus)
        val canStartNGPlus = (ownedAchievements.size % allAchievements.size) == 0 && ownedAchievements.size >= 14
        val achievementDifference = allAchievements.size - ownedAchievements.size

        // Не пришло параметров или пришло неверное название
        if (arguments == null || arguments.isEmpty() || arguments[0] != "start") {
            // Сформировать текст
            result += if (canStartNGPlus) {
                "\n\nВы можете начать ${bold("Новую игру плюс")}!" +
                        "\n${code("/newgameplus start")}\n"
            } else {
                "\nВы пока не можете начать ${bold("Новую игру плюс")}!" +
                        "\n\nОсталось достижений: " +
                        bold("$achievementDifference\n")
            }

            // Отправить ответ в чат
            absSender.execute(
                createReply(
                    chatId = chatId,
                    replyId = replyId,
                    text = result
                )
            )
            return
        }

        // Инициирует новую игру плюс
        if (arguments[0] == "start") {
            if (canStartNGPlus) {
                statService.resetStatsForNewGamePlus(user)
                result = "\n\nВы начали ${bold("Новую игру плюс")}!"
            } else {
                result += "\nВы пока не можете начать ${bold("Новую игру плюс")}!" +
                        "\n\nОсталось достижений: " +
                        bold("$achievementDifference\n")
            }
            // Отправить ответ в чат
            absSender.execute(
                createReply(
                    chatId = chatId,
                    replyId = replyId,
                    text = result
                )
            )
            return
        }
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}