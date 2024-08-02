package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.services.StatService
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
class TopMoneyCommand(
    val statService: StatService
) : BotCommand(ECommands.TOPMONEY.text, ECommands.TOPMONEY.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val topScorers = statService.readTopMoney()
        var result = "${bold(ECommands.TOPMONEY.description)}\n"
        if (topScorers != null) {
            for (i in topScorers.indices) {
                val username = topScorers[i].userId!!.telegramHandle
                val money = topScorers[i].moneyEarned
                result += "\n${i + 1}. $username:   " +
                        "${bold("${moneyFormatter(money)} $")}\n"
            }
        } else {
            result = "\n${emoji(10071)} Записи о лучших игроках не найдены"
        }

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