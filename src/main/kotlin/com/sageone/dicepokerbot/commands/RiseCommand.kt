package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.botStartDate
import com.sageone.dicepokerbot.contextStartDate
import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.createReply
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import java.time.Duration
import java.time.LocalDateTime

@Component
class RiseCommand : BotCommand(ECommands.RISE.text, ECommands.RISE.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {
        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        val contextUptime = Duration.between(contextStartDate, botStartDate)
        val botUptime = Duration.between(botStartDate, LocalDateTime.now())
        var result = bold("Контекст поднялся за: ") +
                "\n${contextUptime.toSecondsPart()}.${contextUptime.toMillisPart()} сек" +
                "\n\n" +
                bold("Бот проработал уже: \n")

        if (botUptime.toDaysPart() > 0)
            result += "${botUptime.toDaysPart()} дн "
        if (botUptime.toHoursPart() > 0)
            result += "${botUptime.toHoursPart()} ч "
        if (botUptime.toMinutesPart() > 0)
            result += "${botUptime.toMinutesPart()} мин "
        if (botUptime.toSecondsPart() > 0)
            result += "${botUptime.toSecondsPart()} сек "
        if (botUptime.toMillisPart() > 0)
            result += "${botUptime.toMillisPart()} мсек "

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