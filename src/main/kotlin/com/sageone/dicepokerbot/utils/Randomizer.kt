package com.sageone.dicepokerbot.utils

import com.sageone.dicepokerbot.enums.EDiceCombos
import kotlin.random.Random

class Randomizer {
    // Бросок кубика (Стандартные кубики: 4, 6, 8, 10, 12, 20)
    fun getDiceRoll(numberOfSides: Int): Int {
        return Random.nextInt(1, numberOfSides + 1)
    }

    fun getDicePokerHand(): IntArray {
        val dice = IntArray(5)
        for (i in 0..4) {
            dice[i] = getDiceRoll(6)
        }
        dice.sort()
        return dice
    }

    fun getHandType(handValues: IntArray): EDiceCombos {
        val diceCount = IntArray(6)
        for (i in 0..4) {
            diceCount[handValues[i] - 1]++
        }

        return when (true) {
            diceCount.contains(5) -> EDiceCombos.POKER
            diceCount.contains(4) -> EDiceCombos.QUAD
            diceCount.contains(3) && diceCount.contains(2) -> EDiceCombos.FULL_HOUSE
            diceCount.contains(3) -> EDiceCombos.TRIPLE
            diceCount.count { it == 2 } == 2 -> EDiceCombos.TWO_PAIR
            diceCount.contains(2) -> EDiceCombos.PAIR
            diceCount[5] == 0 -> EDiceCombos.SMALL_STRAIGHT
            diceCount[0] == 0 -> EDiceCombos.BIG_STRAIGHT
            else -> EDiceCombos.HIGH_CARD
        }
    }

    fun countHandPoints(
        handValues: IntArray,
        handType: EDiceCombos,
        achievementCount: Int,
        luckyRoll: Boolean,
        superMult: Boolean
    ): Int {
        val diceCount = IntArray(6)
        for (i in 0..4) {
            diceCount[handValues[i] - 1]++
        }

        // Бонус за каждый сыгранный кубик
        val handPoints = handValues.sum()

        // Бонус за комбо
        val diceMult = 5
        val comboPoints = when (handType) {
            EDiceCombos.POKER -> (diceCount.indexOf(5) + 1) * 5
            EDiceCombos.QUAD -> (diceCount.indexOf(4) + 1) * 4
            EDiceCombos.FULL_HOUSE -> (diceCount.indexOf(3) + 1) * 3 + (diceCount.indexOf(2) + 1) * 2
            EDiceCombos.TRIPLE -> (diceCount.indexOf(3) + 1) * 3
            EDiceCombos.TWO_PAIR -> ((diceCount.indexOf(2) + 1) + (diceCount.lastIndexOf(2) + 1)) * 2
            EDiceCombos.PAIR -> (diceCount.indexOf(2) + 1) * 2
            else -> 0
        }

        // Бонус за каждое открытое достижение
        val achievementPoints = 250 * achievementCount

        // Бонус за счастливый бросок
        val luckyPoints = if (luckyRoll) {
            when (true) {
                achievementCount == 0 -> 0
                achievementCount in 1..3 -> 100
                achievementCount in 4..8 -> 250
                achievementCount in 9..12 -> 500
                achievementCount in 13..20 -> 1000
                achievementCount in 21..30 -> 2500
                else -> 5000
            }
        } else 0

        // Множитель за все открытые достижения
        val finalMult = if (superMult) {
            2
        } else {
            1
        }

        println(
            "\nОснова за комбо = ${handType.basePoints}" +
                    "\nБонус за комбо = ${(handPoints + comboPoints) * diceMult}" +
                    "\nБонус за ачивки = $achievementPoints" +
                    "\nСчастливый бросок = $luckyPoints" +
                    "\nМножитель за ачивки = $finalMult"
        )

        // Итоговый бонус
        return (handType.basePoints + (handPoints + comboPoints) * diceMult + achievementPoints + luckyPoints) * finalMult
    }

    // Задать вероятность события (от 1 до 100), функция вернет, попали мы в вероятность или нет
    fun getChanceRoll(requiredPercentage: Int): Boolean {
        val correctedRequirement = when (true) {
            requiredPercentage > 100 -> 100
            requiredPercentage < 1 -> 1
            else -> requiredPercentage
        }
        val rollResult = Random.nextInt(1, 101)
        return rollResult <= correctedRequirement
    }

    // Вернет случайный процент от 1 до 100
    fun getPercentage(): Int {
        return Random.nextInt(1, 101)
    }

    // Вернет случайное значение из заданного диапазона (включая оба значения)
    private fun getRandomFromRange(from: Int, to: Int): Int {
        return Random.nextInt(from, to + 1)
    }
}