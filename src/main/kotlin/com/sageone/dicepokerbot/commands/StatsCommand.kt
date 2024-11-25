package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.enums.EStats
import com.sageone.dicepokerbot.services.StatService
import com.sageone.dicepokerbot.services.UserService
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.createReply
import com.sageone.dicepokerbot.utils.emojiWrapper
import com.sageone.dicepokerbot.utils.statsDescriptionWrapper
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@Component
class StatsCommand(
    val userService: UserService,
    val statService: StatService
) : BotCommand(ECommands.STATS.text, ECommands.STATS.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val user = userService.createUser(message)
        val stats = statService.readOrCreateUserStats(user)
        val result = bold(emojiWrapper(128202, "Показатели игрока ${user.telegramHandle}")) +
                "\n" +
                bold("\nПрофиль:\n") +
                statsDescriptionWrapper(EStats.CURRENT_MONEY, stats.currentMoney) + " $\n" +
                statsDescriptionWrapper(EStats.MAX_MONEY, stats.maxMoney) + " $\n" +
                statsDescriptionWrapper(EStats.POINTS, stats.points) + "\n" +
                statsDescriptionWrapper(EStats.HIGHEST_SCORE, stats.highestScore) + "\n" +
                statsDescriptionWrapper(EStats.TOKENS, stats.tokens) + " #\n" +
                statsDescriptionWrapper(EStats.NEW_GAME_PLUS, stats.newGamePlus) + "\n" +

                // TODO: quest stats

                bold("\nКомбо:\n") +
                statsDescriptionWrapper(EStats.HIGH_CARD, stats.highCard) + "\n" +
                statsDescriptionWrapper(EStats.PAIR, stats.pair) + "\n" +
                statsDescriptionWrapper(EStats.TWO_PAIR, stats.twoPair) + "\n" +
                statsDescriptionWrapper(EStats.TRIPLE, stats.triple) + "\n" +
                statsDescriptionWrapper(EStats.FULL_HOUSE, stats.fullHouse) + "\n" +
                statsDescriptionWrapper(EStats.SMALL_STRAIGHT, stats.smallStraight) + "\n" +
                statsDescriptionWrapper(EStats.BIG_STRAIGHT, stats.bigStraight) + "\n" +
                statsDescriptionWrapper(EStats.QUAD, stats.quad) + "\n" +
                statsDescriptionWrapper(EStats.POKER, stats.poker) + "\n" +

                bold("\nКубики:\n") +
                statsDescriptionWrapper(EStats.ONE_PLAYED, stats.onePlayed) + "\n" +
                statsDescriptionWrapper(EStats.TWO_PLAYED, stats.twoPlayed) + "\n" +
                statsDescriptionWrapper(EStats.THREE_PLAYED, stats.threePlayed) + "\n" +
                statsDescriptionWrapper(EStats.FOUR_PLAYED, stats.fourPlayed) + "\n" +
                statsDescriptionWrapper(EStats.FIVE_PLAYED, stats.fivePlayed) + "\n" +
                statsDescriptionWrapper(EStats.SIX_PLAYED, stats.sixPlayed) + "\n" +

                bold("\nПрочее:\n") +
                statsDescriptionWrapper(EStats.HANDS_PLAYED, stats.handsPlayed) + "\n" +
                statsDescriptionWrapper(EStats.WHITE_SET_PLAYED, stats.whiteSetPlayed) + "\n" +
                statsDescriptionWrapper(EStats.OTHER_SETS_PLAYED, stats.handsPlayed - stats.whiteSetPlayed) + "\n" +
                statsDescriptionWrapper(EStats.PAIRS_IN_A_ROW, stats.pairsInARow) + "\n" +
                statsDescriptionWrapper(EStats.MAX_PAIRS_IN_A_ROW, stats.maxPairsInARow) + "\n" +
                statsDescriptionWrapper(EStats.LUCKY_IN_A_ROW, stats.luckyInARow) + "\n" +
                statsDescriptionWrapper(EStats.UNLUCKY_IN_A_ROW, stats.unluckyInARow) + "\n" +
                statsDescriptionWrapper(EStats.MAX_LUCKY_IN_A_ROW, stats.maxLuckyInARow) + "\n"

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