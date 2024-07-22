package com.sageone.dicepokerbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class BotMain(
    @Value("\${telegram.token}")
    token: String,
    commands: Set<BotCommand>,
) : TelegramLongPollingCommandBot(token) {

    init {
        registerAll(*commands.toTypedArray())
    }

    @Value("\${telegram.botName}")
    private val botName: String = ""

    //TODO: проверить, работают ли пути из ямла и экранируются ли \
    @Value("\${filepath.main}")
    val resources: String = ""

    override fun getBotUsername(): String = botName
    override fun processInvalidCommandUpdate(update: Update?) {
        println("Такой команды не существует. MessageId = ${update?.message?.messageId}")
    }
    override fun processNonCommandUpdate(update: Update?) {
        println("Не требует обработки, чат работает только с командами. MessageId = ${update?.message?.messageId}")
    }

}