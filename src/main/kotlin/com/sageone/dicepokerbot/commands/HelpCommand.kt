package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.enums.ECommands
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.createReply
import com.sageone.dicepokerbot.utils.emoji
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class HelpCommand : BotCommand(ECommands.HELP.text, ECommands.HELP.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {
        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        val output = StringBuilder()

        output.append(
            "Бот предназначен для игры в покер на игральных костях ${emoji(127922)}" +
                    "\n\n${bold("Доступные команды:")}\n"
        )
        if (message.chat.isUserChat) {
            ECommands.values().forEach {
                if (!it.isHidden) {
                    output.append("/${it.text} ${it.description}\n")
                }
            }
        }
        else {
            ECommands.values().forEach {
                if (!it.isHidden && !it.onlyForUserChats) {
                    output.append("/${it.text} ${it.description}\n")
                }
            }
        }
        output.append(bold("\n- Бросайте кости и попробуйте набрать больше всех баллов!"))
        output.append(bold("\n- Выполняйте ежедневные задания!"))
        output.append(bold("\n- Зарабатывайте внутриигровые достижения!"))
        output.append(bold("\n- Персонализируйте внешний вид своих кубиков!"))

        absSender.execute(createReply(chatId = chatId, replyId = replyId, text = output.toString()))

    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}