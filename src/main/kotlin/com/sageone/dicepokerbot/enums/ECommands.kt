package com.sageone.dicepokerbot.enums

import com.sageone.dicepokerbot.utils.emojiWrapper

enum class ECommands(
    val text: String,
    val description: String = "",
    val onlyForUserChats: Boolean,
    val isHidden: Boolean
) {
    RISE(
        text = "rise",
        description = emojiWrapper(10068, "Проветрить состояние бота"),
        onlyForUserChats = false,
        isHidden = true),
    HELP(
        text = "help",
        description = emojiWrapper(10068, "Информация о боте"),
        onlyForUserChats = false,
        isHidden = false),
    BUY(
        text = "buy",
        description = emojiWrapper(128179, "Купить набор кубиков"),
        onlyForUserChats = true,
        isHidden = false),
    POKER(
        text = "poker",
        description = emojiWrapper(127922, "Сыграть в покер на костях"),
        onlyForUserChats = false,
        isHidden = false),
    EQUIP(
        text = "equip",
        description = emojiWrapper(128511, "Выбрать внешний вид кубиков"),
        onlyForUserChats = true,
        isHidden = false),
    MYSCORE(
        text = "myscore",
        description = emojiWrapper(128202, "Ваши показатели в игре"),
        onlyForUserChats = true,
        isHidden = false),
    TOPSCORE(
        text = "topscore",
        description = emojiWrapper(128175, "Топ игроков по заработанным баллам"),
        onlyForUserChats = false,
        isHidden = false),
    TOPMONEY(
        text = "topmoney",
        description = emojiWrapper(128181, "Топ игроков по заработанным деньгам"),
        onlyForUserChats = false,
        isHidden = false),
    ACHIEVEMENTS(
        text = "achievements",
        description = emojiWrapper(127942, "Ваш список достижений"),
        onlyForUserChats = true,
        isHidden = false);
}