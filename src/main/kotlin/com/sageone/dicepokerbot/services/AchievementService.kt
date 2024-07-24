package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.BetaTester
import com.sageone.dicepokerbot.EAchievements
import com.sageone.dicepokerbot.EDiceCombos
import com.sageone.dicepokerbot.EStats
import com.sageone.dicepokerbot.database.jpa.entity.AchievementsEntity
import com.sageone.dicepokerbot.database.jpa.entity.StatsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.repositories.AchievementsRepository
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.emoji
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AchievementService {

    @Autowired
    lateinit var achievementsRepository: AchievementsRepository

    @Autowired
    lateinit var betaTester: BetaTester

    fun readOrCreateUserAchievements(user: UserEntity): AchievementsEntity {
        return if (checkUserHasAchievements(user))
            readUserAchievements(user)
        else createAchievements(user)
    }

    fun updateAchievements(achievements: AchievementsEntity) {
        achievementsRepository.save(achievements)
    }

    fun readUserAchievements(user: UserEntity): AchievementsEntity {
        return achievementsRepository.findByUserId(user)
    }

    fun checkUserHasAchievements(user: UserEntity): Boolean {
        return achievementsRepository.countByUserId(user) > 0
    }

    fun achievementsToMap(achievements: AchievementsEntity): MutableMap<EAchievements, Boolean> {
        return mutableMapOf(
            EAchievements.SNAKE_EYES to achievements.snakeEyes!!,
            EAchievements.PEAR to achievements.pear!!,
            EAchievements.AVENUE to achievements.avenue!!,
            EAchievements.LUCKY_NUMBER to achievements.luckyNumber!!,
            EAchievements.LOW_STAKES to achievements.lowStakes!!,
            EAchievements.WHITE_SET to achievements.whiteSet!!,
            EAchievements.BLACK_SET to achievements.blackSet!!,
            EAchievements.BIG_MONEY to achievements.bigMoney!!,
            EAchievements.BIGGER_MONEY to achievements.biggerMoney!!,
            EAchievements.MILLIONAIRE to achievements.millionaire!!,
            EAchievements.KING to achievements.king!!,
            EAchievements.ALPHA to achievements.alpha!!,
            EAchievements.DICE_POKER to achievements.dicePoker!!
        )
    }

    fun achievementsToEntityAndUpdate(
        achievements: AchievementsEntity,
        achievementsMap: MutableMap<EAchievements, Boolean>
    ) {
        achievements.snakeEyes = achievementsMap[EAchievements.SNAKE_EYES]
        achievements.pear = achievementsMap[EAchievements.PEAR]
        achievements.avenue = achievementsMap[EAchievements.AVENUE]
        achievements.luckyNumber = achievementsMap[EAchievements.LUCKY_NUMBER]
        achievements.lowStakes = achievementsMap[EAchievements.LOW_STAKES]
        achievements.whiteSet = achievementsMap[EAchievements.WHITE_SET]
        achievements.blackSet = achievementsMap[EAchievements.BLACK_SET]
        achievements.bigMoney = achievementsMap[EAchievements.BIG_MONEY]
        achievements.biggerMoney = achievementsMap[EAchievements.BIGGER_MONEY]
        achievements.millionaire = achievementsMap[EAchievements.MILLIONAIRE]
        achievements.king = achievementsMap[EAchievements.KING]
        achievements.alpha = achievementsMap[EAchievements.ALPHA]
        achievements.dicePoker = achievementsMap[EAchievements.DICE_POKER]
        updateAchievements(achievements)
    }

    fun analyseAchievements(
        stats: StatsEntity,
        achievementsMap: MutableMap<EAchievements, Boolean>,
        points: Long,
        user: UserEntity
    ): MutableList<EAchievements> {
        val newAchievements = mutableListOf<EAchievements>()
        if (!achievementsMap[EAchievements.SNAKE_EYES]!! && (stats.highCard >= EAchievements.SNAKE_EYES.conditionRequirement)) {
            achievementsMap[EAchievements.SNAKE_EYES] = true
            newAchievements.add(EAchievements.SNAKE_EYES)
        }
        if (!achievementsMap[EAchievements.PEAR]!! && (stats.pairsInARow >= EAchievements.PEAR.conditionRequirement)) {
            achievementsMap[EAchievements.PEAR] = true
            newAchievements.add(EAchievements.PEAR)
        }
        if (!achievementsMap[EAchievements.AVENUE]!! && ((stats.smallStraight + stats.bigStraight) >= EAchievements.AVENUE.conditionRequirement)) {
            achievementsMap[EAchievements.AVENUE] = true
            newAchievements.add(EAchievements.AVENUE)
        }
        if (!achievementsMap[EAchievements.LUCKY_NUMBER]!! && (stats.triple >= EAchievements.LUCKY_NUMBER.conditionRequirement)) {
            achievementsMap[EAchievements.LUCKY_NUMBER] = true
            newAchievements.add(EAchievements.LUCKY_NUMBER)
        }
        if (!achievementsMap[EAchievements.LOW_STAKES]!! &&
            (stats.highCard >= EAchievements.LOW_STAKES.conditionRequirement)
            && (stats.pair >= EAchievements.LOW_STAKES.conditionRequirement)
            && (stats.twoPair >= EAchievements.LOW_STAKES.conditionRequirement)
            && (stats.triple >= EAchievements.LOW_STAKES.conditionRequirement)
        ) {
            achievementsMap[EAchievements.LOW_STAKES] = true
            newAchievements.add(EAchievements.LOW_STAKES)
        }
        if (!achievementsMap[EAchievements.WHITE_SET]!! && stats.handsPlayedWhiteSet >= EAchievements.WHITE_SET.conditionRequirement) {
            achievementsMap[EAchievements.WHITE_SET] = true
            newAchievements.add(EAchievements.WHITE_SET)
        }
        if (!achievementsMap[EAchievements.BLACK_SET]!! && stats.handsPlayedBlackSet >= EAchievements.BLACK_SET.conditionRequirement) {
            achievementsMap[EAchievements.BLACK_SET] = true
            newAchievements.add(EAchievements.BLACK_SET)
        }
        if (!achievementsMap[EAchievements.BIG_MONEY]!! && points >= EAchievements.BIG_MONEY.conditionRequirement) {
            achievementsMap[EAchievements.BIG_MONEY] = true
            newAchievements.add(EAchievements.BIG_MONEY)
        }
        if (!achievementsMap[EAchievements.BIGGER_MONEY]!! && points >= EAchievements.BIGGER_MONEY.conditionRequirement) {
            achievementsMap[EAchievements.BIGGER_MONEY] = true
            newAchievements.add(EAchievements.BIGGER_MONEY)
        }
        if (!achievementsMap[EAchievements.MILLIONAIRE]!! && stats.pointsEarned >= EAchievements.MILLIONAIRE.conditionRequirement) {
            achievementsMap[EAchievements.MILLIONAIRE] = true
            newAchievements.add(EAchievements.MILLIONAIRE)
        }
        if (!achievementsMap[EAchievements.KING]!! && (stats.poker >= EAchievements.KING.conditionRequirement)) {
            achievementsMap[EAchievements.KING] = true
            newAchievements.add(EAchievements.KING)
        }
        if (!achievementsMap[EAchievements.ALPHA]!! &&
            betaTester.id.contains(user.telegramId!!))
        {
            achievementsMap[EAchievements.ALPHA] = true
            newAchievements.add(EAchievements.ALPHA)
        }
        if (!achievementsMap[EAchievements.DICE_POKER]!! &&
            achievementsMap[EAchievements.SNAKE_EYES] == true &&
            achievementsMap[EAchievements.PEAR] == true &&
            achievementsMap[EAchievements.AVENUE] == true &&
            achievementsMap[EAchievements.LUCKY_NUMBER] == true &&
            achievementsMap[EAchievements.LOW_STAKES] == true &&
            achievementsMap[EAchievements.WHITE_SET] == true &&
            achievementsMap[EAchievements.BLACK_SET] == true &&
            achievementsMap[EAchievements.BIG_MONEY] == true &&
            achievementsMap[EAchievements.BIGGER_MONEY] == true &&
            achievementsMap[EAchievements.MILLIONAIRE] == true &&
            achievementsMap[EAchievements.KING] == true)
        {
            achievementsMap[EAchievements.DICE_POKER] = true
            newAchievements.add(EAchievements.DICE_POKER)
        }
        return newAchievements
    }

    fun createAchievements(user: UserEntity): AchievementsEntity {
        val achievements = AchievementsEntity()
        achievements.userId = user
        achievements.snakeEyes = false
        achievements.pear = false
        achievements.avenue = false
        achievements.luckyNumber = false
        achievements.lowStakes = false
        achievements.whiteSet = false
        achievements.blackSet = false
        achievements.bigMoney = false
        achievements.biggerMoney = false
        achievements.millionaire = false
        achievements.king = false
        achievements.alpha = false
        achievements.dicePoker = false
        achievementsRepository.save(achievements)
        return achievements
    }

    fun generateAllAchievementsText(
        user: UserEntity,
        stats: StatsEntity,
        unlockedAchievements: MutableMap<EAchievements, Boolean>
    ): String {
        var result = "${emoji(10068)} " +
                "Каждое разблокированное достижение дает дополнительные баллы " +
                "${bold("[случайное число от 300 до 700]")} к каждому комбо. " +
                "\n${emoji(10068)} " +
                "Если вы получили достижение ${bold(EAchievements.DICE_POKER.publicName)}, " +
                "то баллы каждого комбо увеличатся в 2 раза!" +
                "\n\n${emoji(127942)} " +
                bold("Список достижений игрока ${user.telegramHandle}") +
                " ${emoji(127942)}\n\n"
        var counter = 1
        for (i in EAchievements.values()) {
            result += when (i) {
                EAchievements.SNAKE_EYES,
                EAchievements.PEAR,
                EAchievements.AVENUE,
                EAchievements.LUCKY_NUMBER,
                EAchievements.LOW_STAKES,
                EAchievements.WHITE_SET,
                EAchievements.BLACK_SET,
                EAchievements.KING ->
                    "${
                        achievementEmoji(
                            i,
                            unlockedAchievements
                        )
                    } ${bold("$counter. " + i.publicName)}: \n${i.conditionRequirement}${
                        i.conditionText
                    }${bold(i.conditionName)}\n" +
                            "${achievementProgress(i, stats)}\n\n"
                EAchievements.BIG_MONEY,
                EAchievements.BIGGER_MONEY,
                EAchievements.MILLIONAIRE ->
                    "${
                        achievementEmoji(
                            i,
                            unlockedAchievements
                        )
                    } ${bold("$counter. " + i.publicName)}: \n${i.conditionText}${
                        bold(i.conditionRequirement.toString() + i.conditionName)
                    }\n" +
                            "${achievementProgress(i, stats)}\n\n"
                EAchievements.ALPHA,
                EAchievements.DICE_POKER ->
                    "${
                        achievementEmoji(
                            i,
                            unlockedAchievements
                        )
                    } ${bold("$counter. " + i.publicName)}: \n${i.conditionName}!\n\n"
            }
            counter++
        }
        return result
    }

    fun achievementProgress(
        achievement: EAchievements,
        stats: StatsEntity
    ): String {
        when (achievement) {
            EAchievements.SNAKE_EYES ->
                return "${bold("${achievement.resultText}:")} ${stats.highCard}"
            EAchievements.PEAR ->
                return "${bold("${achievement.resultText}:")} ${stats.pairsInARow}\n" +
                "${bold("${EStats.MAX_PAIRS_IN_A_ROW.description}:")} ${stats.maxPairsInARow}"
            EAchievements.AVENUE ->
                return "${bold("${achievement.resultText}:")} ${stats.smallStraight + stats.bigStraight}"
            EAchievements.LUCKY_NUMBER ->
                return "${bold("${achievement.resultText}:")} ${stats.triple}"
            EAchievements.LOW_STAKES ->
                return "${bold(EDiceCombos.HIGH_CARD.publicName)}: ${stats.highCard}\n" +
                        "${bold(EDiceCombos.PAIR.publicName)}: ${stats.pair}\n" +
                        "${bold(EDiceCombos.TWO_PAIR.publicName)}: ${stats.twoPair}\n" +
                        "${bold(EDiceCombos.TRIPLE.publicName)}: ${stats.triple}"
            EAchievements.WHITE_SET ->
                return "${bold("${achievement.resultText}:")} ${stats.handsPlayedWhiteSet}"
            EAchievements.BLACK_SET ->
                return "${bold("${achievement.resultText}:")} ${stats.handsPlayedBlackSet}"
            EAchievements.BIG_MONEY ->
                return "${bold("${achievement.resultText}:")} ${stats.highestScore}"
            EAchievements.BIGGER_MONEY ->
                return "${bold("${achievement.resultText}:")} ${stats.highestScore}"
            EAchievements.MILLIONAIRE ->
                return "${bold("${achievement.resultText}:")} ${stats.pointsEarned}"
            EAchievements.KING ->
                return "${bold("${achievement.resultText}:")} ${stats.poker}"
            else -> return ""
        }
    }

    fun achievementEmoji(
        achievement: EAchievements,
        unlockedAchievements: MutableMap<EAchievements, Boolean>
    ): String {
        return if (unlockedAchievements.contains(achievement)) {
            emoji(9989)
        } else {
            emoji(10060)
        }
    }

}