package com.sageone.dicepokerbot.commands

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
class EquipCommand(
    val userService: UserService
) : BotCommand(ECommands.EQUIP.text, ECommands.EQUIP.description) {

    override fun processMessage(absSender: AbsSender, message: Message, arguments: Array<out String>?) {

        val chatId = message.chat.id.toString()
        val replyId = message.messageId
        if (message.chat.isUserChat) {
            // Инициализировать
            val user = userService.createUser(message)
            val ownedDiceSets = userService.createOwnedDiceSetMap(user)
            var result = emoji(10071) + "Укажите одно корректное название набора!"

            // Не пришло название набора
            if (arguments == null || arguments.isEmpty()) {
                // Сформировать текст
                result = "${bold("${emoji(128302)} Ваши наборы кубиков ${emoji(128302)}")}\n\n"
                var counter = 1
                for (i in ownedDiceSets) {
                    result += bold("$counter. ${i.value}") +
                            "\n${code("/equip " + i.key)}\n"
                    counter++
                }
                result += "\n" + bold("Сейчас используется набор") + ": ${user.enabledDiceSet!!.publicName}"

                val file = InputFile(generateDiceSetImage(ownedDiceSets))
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

            val diceSets = userService.createDiceSetMap()
            // Пришло неверное название набора
            var incorrectNameCounter = 0
            for (i in diceSets) {
                if (arguments[0] != i.key) {
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

            // Набора нет у пользователя
            if (!ownedDiceSets.contains(arguments[0])) {
                result = emoji(10071) + "Вы еще не купили этот набор!"
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
            val userSet = userService.diceSetRepository.findDiceSetsEntityBySystemName(arguments[0])
            user.enabledDiceSet = userSet
            userService.userRepository.save(user)
            result = "${emoji(128511)} " +
                    "Вы применили новый внешний вид!" +
                    " ${emoji(128511)}"

            // Сгенерировать картинку
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
                    text = emoji(10071) + "смены внешнего вида доступны только в личном чате с ботом!"
                )
            )
        }

    }

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) = Unit
}