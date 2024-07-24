package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.EStats
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
class BuyCommand(
    val userService: UserService,
    val statService: StatService
) : BotCommand(ECommands.BUY.text, ECommands.BUY.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        if (message.chat.isUserChat) {
            // Инициализировать
            val user = userService.createUser(message)
            val stats = statService.readOrCreateUserStats(user)
            val shopDiceSets = userService.createShopDiceSetsMap(user)
            var result = emoji(10071) + "Укажите одно корректное название набора для покупки!"

            // Не пришло название набора для покупки
            if (arguments == null || arguments.isEmpty()) {
                // Сформировать текст
                if (shopDiceSets.isNotEmpty()) {
                    result = "${emoji(127873)} " +
                            "${bold("Вам доступны следующие наборы кубиков")} " +
                            "${emoji(127873)}\n\n"
                    var counter = 1
                    for (i in shopDiceSets) {
                        result += bold("$counter. ${i.value}: ${moneyFormatter(userService.readDiceSetCost(i.key))} $") +
                                "\n${code("/buy " + i.key)}\n"
                        counter++
                    }
                    result += "\n" + bold(EStats.MONEY_EARNED.description) + ": ${moneyFormatter(stats.moneyEarned!!)} $"
                    val file = InputFile(generateDiceSetImage(shopDiceSets))
                    // Отправить ответ в чат
                    absSender.execute(
                        createReplyWithImage(
                            chatId = chatId,
                            replyId = replyId,
                            text = result,
                            image = file
                        )
                    )
                    return
                }

                result = emoji(10071) + bold(" Для вас нет доступных наборов кубиков")
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

            val diceSets = userService.readDiceSets()
            // Пришло неверное название набора
            var incorrectNameCounter = 0
            for (i in diceSets) {
                if (arguments[0] != i.systemName) {
                    incorrectNameCounter++
                }
            }
            if (incorrectNameCounter >= diceSets.size) {
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

            // Набор уже есть у пользователя
            if (!shopDiceSets.contains(arguments[0])) {
                result = emoji(10071) + "У вас уже есть этот набор!" +
                        "\n${code("/equip " + arguments[0])}"
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

            val cost = userService.readDiceSetCost(arguments[0])
            // Не хватает денег на покупку
            if (cost > stats.moneyEarned) {
                result = emoji(10071) + "У вас недостаточно средств для покупки!"
                result += "\n\n" + bold(EStats.MONEY_EARNED.description) + ": ${moneyFormatter(stats.moneyEarned!!)} $"
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

            // Совершить транзакцию
            stats.moneyEarned -= cost
            userService.linkDiceSet(user, listOf(arguments[0]))
            statService.updateStats(stats)
            result = "${emoji(127881)} " +
                    "Набор успешно приобретен!" +
                    " ${emoji(127881)}" +
                    "\n${code("/equip " + arguments[0])}" +
                    "\n\n" + bold(EStats.MONEY_EARNED.description) + ": ${moneyFormatter(stats.moneyEarned!!)} $"

            // Сгенерировать картинку
            val userSet = userService.diceSetRepository.findDiceSetsEntityBySystemName(arguments[0])
            val userSetMap = mutableMapOf(userSet.systemName!! to userSet.publicName!!)
            val file = InputFile(generateDiceSetImage(userSetMap))
            // Отправить ответ в чат
            absSender.execute(
                createReplyWithImage(
                    chatId = chatId,
                    replyId = replyId,
                    text = result,
                    image = file
                )
            )
        } else {
            // Отправить ответ в чат
            absSender.execute(
                createReply(
                    chatId = chatId,
                    replyId = replyId,
                    text = emoji(10071) + "Функции магазина доступны только в личном чате с ботом!"
                )
            )
        }

    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}