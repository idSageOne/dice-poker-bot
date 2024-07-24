package com.sageone.dicepokerbot

enum class EStats(
    val description: String
) {
    HIGH_CARD("Сыграно комбо [${EDiceCombos.HIGH_CARD.publicName}]"),
    PAIR("Сыграно комбо [${EDiceCombos.PAIR.publicName}]"),
    TWO_PAIR("Сыграно комбо [${EDiceCombos.TWO_PAIR.publicName}]"),
    TRIPLE("Сыграно комбо [${EDiceCombos.TRIPLE.publicName}]"),
    FULL_HOUSE("Сыграно комбо [${EDiceCombos.FULL_HOUSE.publicName}]"),
    SMALL_STRAIGHT("Сыграно комбо [${EDiceCombos.SMALL_STRAIGHT.publicName}]"),
    BIG_STRAIGHT("Сыграно комбо [${EDiceCombos.BIG_STRAIGHT.publicName}]"),
    QUAD("Сыграно комбо [${EDiceCombos.QUAD.publicName}]"),
    POKER("Сыграно комбо [${EDiceCombos.POKER.publicName}]"),
    HANDS_PLAYED("Всего сыграно комбо"),
    WHITE_SET("Сыграно комбо с белыми кубиками"),
    BLACK_SET("Сыграно комбо с черными кубиками"),
    PAIRS_IN_A_ROW("Подряд сыграно комбо, в которых есть [${EDiceCombos.PAIR.publicName}]"),
    POINTS_EARNED("Всего заработано баллов"),
    MONEY_EARNED("Всего денег на счету"),
    HIGHEST_SCORE("Лучший персональный счет"),
}