package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.enums.EDiceCombos
import com.sageone.dicepokerbot.enums.EStats
import com.sageone.dicepokerbot.services.AchievementService
import com.sageone.dicepokerbot.services.DiceSetService
import com.sageone.dicepokerbot.services.StatService
import com.sageone.dicepokerbot.services.UserService
import com.sageone.dicepokerbot.utils.*
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class SecretCommand(
    val userService: UserService,
    val statService: StatService,
    val achievementService: AchievementService,
    val diceSetService: DiceSetService
) : BotCommand(ECommands.SECRET.text, ECommands.SECRET.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        // Инициализировать
        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        val viableCommandsList = mutableListOf<String>()
        for (i in EDiceCombos.values()) {
            viableCommandsList.add(i.systemName)
        }
        var text = "Вы нашли\n\n" + bold("${ECommands.SECRET.description}\n\nОднако, секрет не был разгадан!")

        // Не пришло параметров или пришло неверное название
        if (arguments == null || arguments.isEmpty() || !viableCommandsList.contains(arguments[0])) {
            absSender.execute(
                createReply(
                    chatId = chatId,
                    replyId = replyId,
                    text = text
                )
            )
            return
        } else {
            // Пришел корректный параметр
            val randomizer = Randomizer()
            val user = userService.createUser(message)
            val stats = statService.readOrCreateUserStats(user)
            val achievementCount = achievementService.countUserAchievements(user)
            val cost = when (arguments[0]) {
                "poker" -> 5L
                "quad" -> 2L
                else -> 1L
            }
            // Не хватает денег на покупку
            if (cost > stats.tokens) {
                text = emoji(10071) + "Этот секрет вам не по карману!" +
                        "\n\n" + bold(EStats.TOKENS.description) + ": ${moneyFormatter(stats.tokens)} #"
                // Отправить ответ в чат
                absSender.execute(
                    createReply(
                        chatId = chatId,
                        replyId = replyId,
                        text = text
                    )
                )
                return
            }

            // Бросить кости
            val luckyRoll = true
            val superMult = true
            val diceRoll = when (arguments[0]) {
                "highCard" -> arrayOf(1, 3, 4, 5, 6)
                "pair" -> arrayOf(3, 4, 5, 6, 6)
                "twoPair" -> arrayOf(4, 5, 5, 6, 6)
                "triple" -> arrayOf(4, 5, 6, 6, 6)
                "fullHouse" -> arrayOf(5, 5, 6, 6, 6)
                "smallStraight" -> arrayOf(1, 2, 3, 4, 5)
                "bigStraight" -> arrayOf(2, 3, 4, 5, 6)
                "quad" -> arrayOf(5, 6, 6, 6, 6)
                else -> arrayOf(6, 6, 6, 6, 6)
            }
            var handType: EDiceCombos = EDiceCombos.POKER
            for (i in EDiceCombos.values()) {
                if (i.systemName == arguments[0]) {
                    handType = i
                }
            }
            val points =
                2 * randomizer.countHandPoints(diceRoll.toIntArray(), handType, achievementCount, luckyRoll, superMult)
                    .toLong()
            val diceSet = diceSetService.readUserEnabledDiceSet(user)

            // Обновить стату в БД
            val highScore: Boolean = stats.highestScore < points
            stats.tokens -= cost
            statService.changeAndUpdateStats(
                stats = stats,
                diceRoll = diceRoll.toIntArray(),
                handType = handType,
                luckyRoll = luckyRoll,
                points = points,
                money = points,
                diceSet = diceSet.systemName
            )

            // Обновить ачивки в БД
            val newAchievements = achievementService.changeAndUpdateAchievements(
                stats = stats,
                user = user
            )

            // Отправить картинку в чат
            val file = InputFile(generatePokerImage(diceRoll.toIntArray(), diceSet.systemName))
            text = "${bold(handType.comboText)} ${moneyFormatter(points)} баллов" +
                    "\n\n${bold(italic("${emoji(128520)} Счастливый бросок ${emoji(128520)} "))}"

            if (highScore) {
                text += "\n\n${bold(italic("${emoji(128520)} Лучший персональный счет ${emoji(128520)} "))}"
            }
            when (true) {
                newAchievements.size == 1 -> {
                    text += "\n\n${bold(italic("${emoji(128520)} Получено новое достижение ${emoji(128520)}"))}"
                    text += "\n${bold(newAchievements[0].publicName)}"
                    text += "\n\n${italic("Подробнее:")} /${ECommands.ACHIEVEMENTS.text}"
                }
                newAchievements.size > 1 -> {
                    text += "\n\n${bold(italic("${emoji(128520)} Получены новые достижения ${emoji(128520)}"))}"
                    var counter = 1
                    for (i in newAchievements) {
                        text += bold("\n$counter. ${i.publicName}")
                        counter++
                    }
                    text += "\n\n${italic("Подробнее:")} /${ECommands.ACHIEVEMENTS.text}"
                }
            }

            text += "\n\n" + bold(EStats.TOKENS.description) + ": ${moneyFormatter(stats.tokens)} #"

            absSender.execute(
                createReplyWithImage(
                    chatId = chatId,
                    replyId = replyId,
                    text = text,
                    image = file
                )
            )
        }
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}