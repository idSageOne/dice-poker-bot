package com.sageone.dicepokerbot.enums

enum class EAchievements(
    val systemName: String = "",
    val publicName: String = "",
    val conditionRequirement: Int,
    val conditionName: String = "",
    val conditionText: String = "",
    val resultText: String = ""
) {
    SNAKE_EYES(
        systemName = "snakeEyes",
        publicName = "Змеиный глаз",
        conditionRequirement = 111,
        conditionName = EDiceCombos.HIGH_CARD.publicName,
        conditionText = " раз сыграть комбо ",
        resultText = EStats.HIGH_CARD.description
    ),
    PEAR(
        systemName = "pear",
        publicName = "Груша",
        conditionRequirement = 22,
        conditionName = EDiceCombos.PAIR.publicName,
        conditionText = " раза подряд сыграть комбо, в котором есть ",
        resultText = EStats.PAIRS_IN_A_ROW.description
    ),
    AVENUE(
        systemName = "avenue",
        publicName = "Авеню",
        conditionRequirement = 62,
        conditionName = "Стрит",
        conditionText = " раза сыграть комбо ",
        resultText = "Сыграно комбо [${EDiceCombos.SMALL_STRAIGHT.publicName}] и [${EDiceCombos.BIG_STRAIGHT.publicName}]"
    ),
    LUCKY_NUMBER(
        systemName = "luckyNumber",
        publicName = "Счастливое число",
        conditionRequirement = 333,
        conditionName = EDiceCombos.TRIPLE.publicName,
        conditionText = " раза сыграть комбо ",
        resultText = EStats.TRIPLE.description
    ),
    LOW_STAKES(
        systemName = "lowStakes",
        publicName = "Низкая ставка",
        conditionRequirement = 100,
        conditionName = "[${EDiceCombos.HIGH_CARD.publicName}, " +
                "${EDiceCombos.PAIR.publicName}, " +
                "${EDiceCombos.TWO_PAIR.publicName}, " +
                "${EDiceCombos.TRIPLE.publicName}]",
        conditionText = " раз сыграть каждое низшее комбо "
    ),
    WHITE_SET(
        systemName = "whiteSet",
        publicName = "Белый набор",
        conditionRequirement = 500,
        conditionName = "стандартным набором кубиков",
        conditionText = " раз сыграть комбо со ",
        resultText = EStats.WHITE_SET.description
    ),
    BLACK_SET(
        systemName = "blackSet",
        publicName = "Черный набор",
        conditionRequirement = 500,
        conditionName = "черным набором кубиков",
        conditionText = " раз сыграть комбо с ",
        resultText = EStats.BLACK_SET.description
    ),
    BIG_MONEY(
        systemName = "bigMoney",
        publicName = "Большая сумма",
        conditionRequirement = 5000,
        conditionName = " баллов",
        conditionText = "За одно комбо набрать ",
        resultText = EStats.HIGHEST_SCORE.description
    ),
    BIGGER_MONEY(
        systemName = "biggerMoney",
        publicName = "Еще бОльшая сумма",
        conditionRequirement = 10000,
        conditionName = " баллов",
        conditionText = "За одно комбо набрать ",
        resultText = EStats.HIGHEST_SCORE.description
    ),
    MILLIONAIRE(
        systemName = "millionaire",
        publicName = "Миллионер",
        conditionRequirement = 1000000,
        conditionName = " баллов",
        conditionText = "Набрать ",
        resultText = EStats.POINTS_EARNED.description
    ),
    KING(
        systemName = "king",
        publicName = "Король",
        conditionRequirement = 1,
        conditionName = EDiceCombos.POKER.publicName,
        conditionText = " раз сыграть комбо ",
        resultText = EStats.POKER.description
    ),
    ALPHA(
        systemName = "alpha",
        publicName = "Альфа",
        conditionRequirement = 0,
        conditionName = "Поучаствовать в бета-тесте"
    ),
    DICE_POKER(
        systemName = "dicePoker",
        publicName = "Покер на костях",
        conditionRequirement = 0,
        conditionName = "Пройти игру"
    ),
}