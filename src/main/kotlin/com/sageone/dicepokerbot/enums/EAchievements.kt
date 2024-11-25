package com.sageone.dicepokerbot.enums

enum class EAchievements(
    val keyword: String = "",
    val conditionText: String = " сыграть комбо ",
    val conditionName: String = "",
    val resultText: String = ""
) {
    SNAKE_EYES(
        keyword = "раз",
        conditionName = EDiceCombos.HIGH_CARD.publicName,
        resultText = EStats.HIGH_CARD.description
    ),
    PEAR(
        keyword = "раз",
        conditionText = " подряд сыграть комбо, в котором есть ",
        conditionName = EDiceCombos.PAIR.publicName,
        resultText = EStats.PAIRS_IN_A_ROW.description
    ),
    LUCKY_NUMBER(
        keyword = "раз",
        conditionName = EDiceCombos.TRIPLE.publicName,
        resultText = EStats.TRIPLE.description
    ),
    CLOVER(
        keyword = "раз",
        conditionText = " подряд сделать ",
        conditionName = "счастливый бросок",
        resultText = EStats.LUCKY_IN_A_ROW.description
    ),
    AVENUE(
        keyword = "раз",
        conditionName = "Стрит",
        resultText = "Сыграно комбо [${EDiceCombos.SMALL_STRAIGHT.publicName}] и [${EDiceCombos.BIG_STRAIGHT.publicName}]"
    ),
    LOW_STAKES(
        keyword = "раз",
        conditionText = " сыграть каждое низшее комбо ",
        conditionName = "[${EDiceCombos.HIGH_CARD.publicName}, " +
                "${EDiceCombos.PAIR.publicName}, " +
                "${EDiceCombos.TWO_PAIR.publicName}, " +
                "${EDiceCombos.TRIPLE.publicName}]",
        resultText = "Всего сыграно низших комбо"
    ),
    HOME_RUN(
        conditionText = "По очереди сыграть комбо ",
        conditionName = "[Стрит, ${EDiceCombos.FULL_HOUSE.publicName}]"
    ),
    WHITE_SET(
        keyword = "раз",
        conditionName = " со стандартным набором кубиков",
        resultText = EStats.WHITE_SET_PLAYED.description
    ),
    BOOK_COVER(
        keyword = "раз",
        conditionName = " с любым набором кубиков, кроме стандартного",
        resultText = EStats.OTHER_SETS_PLAYED.description
    ),
    HANDY(
        keyword = "раз",
        conditionText = "Бросить кости ",
        resultText = EStats.HANDS_PLAYED.description
    ),
    BIG_LEAGUES(
        keyword = "балл",
        conditionText = "За одно комбо набрать ",
        resultText = EStats.HIGHEST_SCORE.description
    ),
    MONEY_MAKER(
        conditionText = "Накопить ",
        conditionName = " $",
        resultText = EStats.CURRENT_MONEY.description
    ),
    KING(
        keyword = "раз",
        conditionName = EDiceCombos.POKER.publicName,
        resultText = EStats.POKER.description
    ),
    DICE_POKER(
        conditionName = "Пройти игру?"
    );

}