package com.sageone.dicepokerbot

enum class EStats(
    val systemName: String,
    val description: String
) {
    HIGH_CARD("highCard", "Сыграно комбо [${EDiceCombos.HIGH_CARD.publicName}]"),
    PAIR("pair", "Сыграно комбо [${EDiceCombos.PAIR.publicName}]"),
    TWO_PAIR("twoPair", "Сыграно комбо [${EDiceCombos.TWO_PAIR.publicName}]"),
    TRIPLE("triple", "Сыграно комбо [${EDiceCombos.TRIPLE.publicName}]"),
    FULL_HOUSE("fullHouse", "Сыграно комбо [${EDiceCombos.FULL_HOUSE.publicName}]"),
    SMALL_STRAIGHT("smallStraight", "Сыграно комбо [${EDiceCombos.SMALL_STRAIGHT.publicName}]"),
    BIG_STRAIGHT("bigStraight", "Сыграно комбо [${EDiceCombos.BIG_STRAIGHT.publicName}]"),
    QUAD("quad", "Сыграно комбо [${EDiceCombos.QUAD.publicName}]"),
    POKER("poker", "Сыграно комбо [${EDiceCombos.POKER.publicName}]"),
    HANDS_PLAYED("handsPlayed", "Всего сыграно комбо"),
    WHITE_SET("handsPlayedWhiteSet", "Сыграно комбо с белыми кубиками"),
    BLACK_SET("handsPlayedBlackSet", "Сыграно комбо с черными кубиками"),
    PAIRS_IN_A_ROW("pairsInARow", "Подряд сыграно комбо, в которых есть [${EDiceCombos.PAIR.publicName}]"),
    POINTS_EARNED("pointsEarned", "Заработано баллов"),
    MONEY_EARNED("moneyEarned", "Заработано денег"),
    HIGHEST_SCORE("highestScore", "Лучший персональный счет"),
}