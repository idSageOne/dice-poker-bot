package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
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
class PokerCommand(
    val userService: UserService,
    val statService: StatService,
    val achievementService: AchievementService,
    val diceSetService: DiceSetService
) : BotCommand(ECommands.POKER.text, ECommands.POKER.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        // Инициализировать
        val randomizer = Randomizer()
        val user = userService.createUser(message)
        val stats = statService.readOrCreateUserStats(user)
        val achievementCount = achievementService.countUserAchievements(user)

        // Бросить кости
        val luckyRoll = randomizer.getChanceRoll(achievementCount + (stats.unluckyInARow.toInt() * 10))
        val diceRoll = randomizer.getDicePokerHand()
        val handType = randomizer.getHandType(diceRoll)
        val superMult = achievementService.checkUserHasSuperMult(user, stats.newGamePlus)
        val points = randomizer.countHandPoints(diceRoll, handType, achievementCount, luckyRoll, superMult).toLong()
        val money = (points / 1000L) * 100L
        val diceSet = diceSetService.readUserEnabledDiceSet(user)

        // Обновить стату в БД
        val highScore: Boolean = stats.highestScore < points
        statService.changeAndUpdateStats(
            stats = stats,
            diceRoll = diceRoll,
            handType = handType,
            luckyRoll = luckyRoll,
            points = points,
            money = money,
            diceSet = diceSet.systemName
        )

        // Обновить ачивки в БД
        val newAchievements = achievementService.changeAndUpdateAchievements(
            stats = stats,
            user = user
        )

        // Отправить картинку в чат
        val file = InputFile(generatePokerImage(diceRoll, diceSet.systemName))
        var text = "${bold(handType.comboText)} ${moneyFormatter(points)} баллов"
        if (luckyRoll) {
            text += "\n\n${bold(italic("${emoji(127808)} Счастливый бросок ${emoji(127808)} "))}"
        }
        if (highScore) {
            text += "\n\n${bold(italic("${emoji(128175)} Лучший персональный счет ${emoji(128175)} "))}"
        }
        when (true) {
            newAchievements.size == 1 -> {
                text += "\n\n${bold(italic("${emoji(127941)} Получено новое достижение ${emoji(127941)}"))}"
                text += "\n${bold(newAchievements[0].publicName)}"
                text += "\n\n${italic("Подробнее:")} /${ECommands.ACHIEVEMENTS.text}"
            }
            newAchievements.size > 1 -> {
                text += "\n\n${bold(italic("${emoji(127941)} Получены новые достижения ${emoji(127941)}"))}"
                var counter = 1
                for (i in newAchievements) {
                    text += bold("\n$counter. ${i.publicName}")
                    counter++
                }
                text += "\n\n${italic("Подробнее:")} /${ECommands.ACHIEVEMENTS.text}"
            }
        }
        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        absSender.execute(
            createReplyWithImage(
                chatId = chatId,
                replyId = replyId,
                text = text,
                image = file
            )
        )
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}