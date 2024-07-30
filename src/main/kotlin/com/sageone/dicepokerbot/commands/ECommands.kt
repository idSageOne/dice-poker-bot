package com.sageone.dicepokerbot.commands

import com.sageone.dicepokerbot.utils.emoji

enum class ECommands(
    val text: String,
    val description: String = "",
    val isGroupChat: Boolean? = true
) {
    RISE("rise", "${emoji(10068)} Проветрить состояние бота ${emoji(10068)}"),
    HELP("help", "${emoji(10068)} Информация о боте ${emoji(10068)}", null),
    BUY("buy", "${emoji(128179)} Купить набор кубиков ${emoji(128179)}",false),
    POKER("poker", "${emoji(127922)} Сыграть в покер на костях ${emoji(127922)}"),
    EQUIP("equip", "${emoji(128511)} Выбрать внешний вид кубиков ${emoji(128511)}", false),
    MYSCORE("myscore", "${emoji(128202)} Ваши показатели в игре ${emoji(128202)}", null),
    TOPSCORE("topscore", "${emoji(128175)} Топ игроков по заработанным баллам ${emoji(128175)}"),
    TOPMONEY("topmoney", "${emoji(128181)} Топ игроков по заработанным деньгам ${emoji(128181)}"),
    ACHIEVEMENTS("achievements", "${emoji(127942)} Ваш список достижений ${emoji(127942)}", null),
}