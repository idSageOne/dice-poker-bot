package com.sageone.dicepokerbot.enums

enum class EStats(
    val description: String = ""
) {
    // User profile stats
    LAST_COMBO(),
    CURRENT_MONEY("Всего денег на счету"),
    MAX_MONEY("Всего заработано денег за всю игру"),
    POINTS("Всего заработано баллов"),
    HIGHEST_SCORE("Лучший персональный бросок"),
    TOKENS("Всего токенов на счету"),
    NEW_GAME_PLUS("Новая игра плюс"),

    // Quest stats
    QUESTS_COMPLETED(),
    COMMON_QUESTS(),
    RARE_QUESTS(),
    EPIC_QUESTS(),
    LEGENDARY_QUESTS(),

    // Combo stats
    HIGH_CARD("Сыграно комбо [${EDiceCombos.HIGH_CARD.publicName}]"),
    PAIR("Сыграно комбо [${EDiceCombos.PAIR.publicName}]"),
    TWO_PAIR("Сыграно комбо [${EDiceCombos.TWO_PAIR.publicName}]"),
    TRIPLE("Сыграно комбо [${EDiceCombos.TRIPLE.publicName}]"),
    FULL_HOUSE("Сыграно комбо [${EDiceCombos.FULL_HOUSE.publicName}]"),
    SMALL_STRAIGHT("Сыграно комбо [${EDiceCombos.SMALL_STRAIGHT.publicName}]"),
    BIG_STRAIGHT("Сыграно комбо [${EDiceCombos.BIG_STRAIGHT.publicName}]"),
    QUAD("Сыграно комбо [${EDiceCombos.QUAD.publicName}]"),
    POKER("Сыграно комбо [${EDiceCombos.POKER.publicName}]"),

    // Dice stats
    ONE_PLAYED("Сыграно кубиков с номиналом 1"),
    TWO_PLAYED("Сыграно кубиков с номиналом 2"),
    THREE_PLAYED("Сыграно кубиков с номиналом 3"),
    FOUR_PLAYED("Сыграно кубиков с номиналом 4"),
    FIVE_PLAYED("Сыграно кубиков с номиналом 5"),
    SIX_PLAYED("Сыграно кубиков с номиналом 6"),

    // Other stats
    HANDS_PLAYED("Всего сыграно комбо"),
    WHITE_SET_PLAYED("Сыграно комбо с белым набором кубиков"),
    OTHER_SETS_PLAYED("Сыграно комбо с купленными наборами кубиков"),
    PAIRS_IN_A_ROW("Сейчас подряд сыграно комбо, в которых есть [${EDiceCombos.PAIR.publicName}]"),
    MAX_PAIRS_IN_A_ROW("Максимум подряд сыграно комбо, в которых есть [${EDiceCombos.PAIR.publicName}]"),
    LUCKY_IN_A_ROW("Сейчас подряд сыграно счастливых бросков"),
    UNLUCKY_IN_A_ROW("Сейчас подряд сыграно несчастливых бросков"),
    MAX_LUCKY_IN_A_ROW("Максимум подряд сыграно счастливых бросков")

}