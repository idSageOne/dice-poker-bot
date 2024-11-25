package com.sageone.dicepokerbot.utils

import com.sageone.dicepokerbot.enums.EStats
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile

fun createReply(chatId: String, text: String, replyId: Int? = null, html: Boolean = true) =
    SendMessage(chatId, text)
        .apply { enableMarkdown(false) }
        .apply { enableHtml(html) }
        .apply { disableWebPagePreview() }
        .apply { replyToMessageId = replyId }

fun createReplyWithImage(chatId: String, text: String, image: InputFile, replyId: Int? = null) =
    SendPhoto(chatId, image)
        .apply { caption = text }
        .apply { parseMode = "html" }
        .apply { replyToMessageId = replyId }

fun emoji(decimalCode: Int): String {
    return String(Character.toChars(decimalCode))
}

fun emojiWrapper(decimalCode: Int, text: String): String {
    return "${emoji(decimalCode)} $text ${emoji(decimalCode)}"
}

fun statsDescriptionWrapper(statsEnumValue: EStats, stats: Long): String {
    return "${statsEnumValue.description}: " + bold(moneyFormatter(stats))
}

fun moneyFormatter(cost: Long): String {
    val moneyFlipped = cost.toString().reversed()
    var result = ""
    for (i in moneyFlipped.indices) {
        result += moneyFlipped[i]
        if ((i + 1) % 3 == 0) {
            result += " "
        }
    }
    return result.reversed()
}

fun keywordWrapper(number: Long, keyword: String): String {
    if (number > 0) {
        val numberString = number.toString()
        val parsedKeyword = when (keyword) {
            "раз" ->
                when (true) {
                    ((numberString.takeLast(1) == "2") && numberString.takeLast(2) != "12") ||
                            ((numberString.takeLast(1) == "3") && numberString.takeLast(2) != "13") ||
                            ((numberString.takeLast(1) == "4") && numberString.takeLast(2) != "14") ->
                        "раза"
                    else -> "раз"
                }
            "балл" ->
                when (true) {
                    ((numberString.takeLast(1) == "1") && numberString.takeLast(2) != "11") ->
                        "балл"
                    ((numberString.takeLast(1) == "2") && numberString.takeLast(2) != "12") ||
                            ((numberString.takeLast(1) == "3") && numberString.takeLast(2) != "13") ||
                            ((numberString.takeLast(1) == "4") && numberString.takeLast(2) != "14") ->
                        "балла"
                    else -> "баллов"
                }
            else -> ""
        }
        return "$numberString $parsedKeyword"
    } else return ""
}

fun bold(text: String): String {
    return "<b>$text</b>"
}

fun italic(text: String): String {
    return "<i>$text</i>"
}

fun code(text: String): String {
    return "<code>$text</code>"
}