package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.EStats
import com.sageone.dicepokerbot.services.StatService
import com.sageone.dicepokerbot.services.UserService
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.createReply
import com.sageone.dicepokerbot.utils.emoji
import com.sageone.dicepokerbot.utils.moneyFormatter
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class MyScoreCommand(
    val userService: UserService,
    val statService: StatService
) : BotCommand(ECommands.MYSCORE.text, ECommands.MYSCORE.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val user = userService.createUser(message)
        val stats = statService.readOrCreateUserStats(user)
        val result = "${emoji(128202)} ${bold("Показатели игрока ${user.telegramHandle}")} ${emoji(128202)}\n\n" +
                "${bold("${EStats.HIGH_CARD.description}:")} ${moneyFormatter(stats.highCard!!)}\n" +
                "${bold("${EStats.PAIR.description}:")} ${moneyFormatter(stats.pair!!)}\n" +
                "${bold("${EStats.TWO_PAIR.description}:")} ${moneyFormatter(stats.twoPair!!)}\n" +
                "${bold("${EStats.TRIPLE.description}:")} ${moneyFormatter(stats.triple!!)}\n" +
                "${bold("${EStats.FULL_HOUSE.description}:")} ${moneyFormatter(stats.fullHouse!!)}\n" +
                "${bold("${EStats.SMALL_STRAIGHT.description}:")} ${moneyFormatter(stats.smallStraight!!)}\n" +
                "${bold("${EStats.BIG_STRAIGHT.description}:")} ${moneyFormatter(stats.bigStraight!!)}\n" +
                "${bold("${EStats.QUAD.description}:")} ${moneyFormatter(stats.quad!!)}\n" +
                "${bold("${EStats.POKER.description}:")} ${moneyFormatter(stats.poker!!)}\n" +
                "${bold("${EStats.HANDS_PLAYED.description}:")} ${moneyFormatter(stats.handsPlayed!!)}\n" +
                "${bold("${EStats.WHITE_SET.description}:")} ${moneyFormatter(stats.handsPlayedWhiteSet!!)}\n" +
                "${bold("${EStats.BLACK_SET.description}:")} ${moneyFormatter(stats.handsPlayedBlackSet!!)}\n" +
                "${bold("${EStats.PAIRS_IN_A_ROW.description}:")} ${moneyFormatter(stats.pairsInARow!!)}\n" +
                "${bold("${EStats.POINTS_EARNED.description}:")} ${moneyFormatter(stats.pointsEarned!!)}\n" +
                "${bold("${EStats.MONEY_EARNED.description}:")} ${moneyFormatter(stats.moneyEarned!!)} $\n" +
                "${bold("${EStats.HIGHEST_SCORE.description}:")} ${moneyFormatter(stats.highestScore!!)}\n"

        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        absSender.execute(
            createReply(
                chatId = chatId,
                replyId = replyId,
                text = result
            )
        )
    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}