package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.AchievementEntity
import com.sageone.dicepokerbot.database.jpa.entity.StatsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersAchievementsEntity
import com.sageone.dicepokerbot.database.jpa.repositories.AchievementRepository
import com.sageone.dicepokerbot.database.jpa.repositories.UsersAchievementsRepository
import com.sageone.dicepokerbot.enums.EAchievements
import com.sageone.dicepokerbot.enums.EDiceCombos
import com.sageone.dicepokerbot.enums.EStats
import com.sageone.dicepokerbot.roles.AlphaTester
import com.sageone.dicepokerbot.roles.BetaTester
import com.sageone.dicepokerbot.utils.bold
import com.sageone.dicepokerbot.utils.emoji
import com.sageone.dicepokerbot.utils.emojiWrapper
import com.sageone.dicepokerbot.utils.keywordWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class AchievementService {

    @Autowired
    lateinit var achievementRepository: AchievementRepository

    @Autowired
    lateinit var usersAchievementsRepository: UsersAchievementsRepository

    @Autowired
    lateinit var alphaTester: AlphaTester

    @Autowired
    lateinit var betaTester: BetaTester

    // Связка пользователей и достижений
    fun linkAchievementToUser(
        user: UserEntity,
        achievement: AchievementEntity,
        newGamePlus: Long,
    ) {
        if (!checkUserHasAchievement(user, achievement, newGamePlus)) {
            val linkedAchievement = UsersAchievementsEntity()
            linkedAchievement.userId = user
            linkedAchievement.achievementId = achievement
            linkedAchievement.newGamePlus = newGamePlus
            usersAchievementsRepository.save(linkedAchievement)
            println("linkAchievementToUser: ${linkedAchievement.achievementId!!.systemName}")
        }
    }

    fun readUserAchievements(user: UserEntity, newGamePlus: Long): MutableList<AchievementEntity> {
        val currentUserAchievementsList =
            usersAchievementsRepository.findAllByUserIdAndAndNewGamePlus(user, newGamePlus)
        val result = mutableListOf<AchievementEntity>()
        for (i in currentUserAchievementsList) {
            result.add(i.achievementId!!)
        }
        return result
    }

    fun readAchievements(): MutableList<AchievementEntity> {
        return achievementRepository.findAll()
    }

    fun checkUserHasAchievement(
        user: UserEntity,
        achievement: AchievementEntity,
        newGamePlus: Long
    ): Boolean {
        return usersAchievementsRepository.countByUserIdAndAchievementIdAndNewGamePlus(
            user,
            achievement,
            newGamePlus
        ) > 0
    }

    fun checkUserHasAchievementBySystemName(
        user: UserEntity,
        achievementSystemName: String,
        newGamePlus: Long
    ): Boolean {
        val achievement = achievementRepository.findBySystemName(achievementSystemName)
        return checkUserHasAchievement(user, achievement, newGamePlus)
    }

    fun countUserAchievements(user: UserEntity): Int {
        return usersAchievementsRepository.countByUserId(user)
    }

    fun checkUserHasSuperMult(user: UserEntity, newGamePlus: Long): Boolean {
        for (i in 0..newGamePlus + 1) {
            if (checkUserHasAchievementBySystemName(user, "dicePoker", i)) {
                return true
            }
        }
        return false
    }

    fun changeAndUpdateAchievements(
        stats: StatsEntity,
        user: UserEntity
    ): MutableList<AchievementEntity> {
        val newAchievements = mutableListOf<AchievementEntity>()
        val achievements = readAchievements()
        val ngPlus = stats.newGamePlus
        for (i in achievements) {
            when (i.systemName) {
                "snakeEyes" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.highCard >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "pear" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.pairsInARow >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "luckyNumber" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.triple >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "clover" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.luckyInARow >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "avenue" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.smallStraight + stats.bigStraight >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "lowStakes" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        (stats.highCard >= i.requirement)
                        && (stats.pair >= i.requirement)
                        && (stats.twoPair >= i.requirement)
                        && (stats.triple >= i.requirement)
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "homeRun" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.lastCombo == "homeRun"
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "whiteSet" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.whiteSetPlayed >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "bookCover" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.handsPlayed - stats.whiteSetPlayed >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "handy" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.handsPlayed >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "bigLeagues" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.highestScore >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "moneyMaker" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.currentMoney >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "king" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        stats.poker >= i.requirement
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
                "dicePoker" ->
                    if (!checkUserHasAchievement(user, i, ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "snakeEyes", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "pear", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "luckyNumber", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "clover", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "avenue", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "lowStakes", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "homeRun", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "whiteSet", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "bookCover", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "handy", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "bigLeagues", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "moneyMaker", ngPlus) &&
                        checkUserHasAchievementBySystemName(user, "king", ngPlus)
                    ) {
                        linkAchievementToUser(user, i, ngPlus)
                        newAchievements.add(i)
                    }
            }
        }
        return newAchievements
    }

    fun generateAllAchievementsText(
        user: UserEntity,
        stats: StatsEntity
    ): String {
        val achievements = readAchievements()

        var result = "${emoji(10068)} " +
                "Каждое разблокированное достижение дает ${bold("250")} " +
                "дополнительных баллов к каждому комбо. " +
                "\n${emoji(10068)} " +
                "Если вы пройдете игру, то баллы каждого комбо увеличатся в 2 раза!" +
                "\n\n" +
                newGamePlusTextHelper(stats) +
                emojiWrapper(127942, bold("Список достижений игрока ${user.telegramHandle}")) +
                "\n\n"

        var counter = 1
        for (i in achievements) {
            result += achievementEmojiHelper(user, i, stats.newGamePlus) + achievementCounterHelper(
                counter,
                i.publicName
            )
            result += when (i.systemName) {
                "snakeEyes" -> achievementRequirement(EAchievements.SNAKE_EYES, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.SNAKE_EYES, stats)

                "pear" -> achievementRequirement(EAchievements.PEAR, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.PEAR, stats)

                "luckyNumber" -> achievementRequirement(EAchievements.LUCKY_NUMBER, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.LUCKY_NUMBER, stats)

                "clover" -> achievementRequirement(EAchievements.CLOVER, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.CLOVER, stats)

                "avenue" -> achievementRequirement(EAchievements.AVENUE, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.AVENUE, stats)

                "lowStakes" -> achievementRequirement(EAchievements.LOW_STAKES, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.LOW_STAKES, stats)

                "homeRun" -> achievementRequirement(EAchievements.HOME_RUN, i.requirement)

                "whiteSet" -> achievementRequirement(EAchievements.WHITE_SET, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.WHITE_SET, stats)

                "bookCover" -> achievementRequirement(EAchievements.BOOK_COVER, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.BOOK_COVER, stats)

                "handy" -> achievementRequirement(EAchievements.HANDY, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.HANDY, stats)

                "bigLeagues" -> achievementRequirement(EAchievements.BIG_LEAGUES, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.BIG_LEAGUES, stats)

                "moneyMaker" -> achievementRequirement(EAchievements.MONEY_MAKER, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.MONEY_MAKER, stats)

                "king" -> achievementRequirement(EAchievements.KING, i.requirement) +
                        "\n" +
                        achievementProgress(EAchievements.KING, stats)

                "dicePoker" -> achievementRequirement(EAchievements.DICE_POKER, i.requirement)
                else -> ""
            }
            result += "\n\n"
            counter++
        }
        result += totalAchievementCountHelper(user)
        return result
    }

    private fun achievementCounterHelper(orderCount: Int, name: String): String {
        return "${bold(" $orderCount. $name")}: \n"
    }

    fun achievementRequirement(
        achievement: EAchievements,
        requirementValue: Long
    ): String {
        return when (achievement) {
            EAchievements.DICE_POKER ->
                achievementRequirementTextHelper(
                    achievement,
                    requirementValue,
                    ""
                )
            EAchievements.HOME_RUN,
            EAchievements.HANDY,
            EAchievements.BIG_LEAGUES,
            EAchievements.MONEY_MAKER ->
                achievementRequirementTextHelper(
                    achievement,
                    requirementValue,
                    "Сначала текст"
                )
            else -> achievementRequirementTextHelper(
                achievement,
                requirementValue,
                "Сначала цифра"
            )
        }
    }

    private fun achievementRequirementTextHelper(
        achievement: EAchievements,
        requirementValue: Long,
        messageType: String
    ): String {
        return when (messageType) {
            "Сначала цифра" ->
                keywordWrapper(requirementValue, achievement.keyword) +
                        achievement.conditionText + bold(achievement.conditionName)
            "Сначала текст" -> achievement.conditionText +
                    bold(keywordWrapper(requirementValue, achievement.keyword)) +
                    bold(achievement.conditionName)
            else -> achievement.conditionName
        }
    }

    fun achievementProgress(
        achievement: EAchievements,
        stats: StatsEntity
    ): String {
        when (achievement) {
            EAchievements.SNAKE_EYES ->
                return achievementProgressTextHelper(achievement, stats.highCard)
            EAchievements.PEAR ->
                return achievementProgressTextHelper(achievement, stats.pairsInARow) +
                        "\n" +
                        "${bold("${EStats.MAX_PAIRS_IN_A_ROW.description}:")} " +
                        "${stats.maxPairsInARow}"
            EAchievements.LUCKY_NUMBER ->
                return achievementProgressTextHelper(achievement, stats.triple)
            EAchievements.CLOVER ->
                return achievementProgressTextHelper(achievement, stats.luckyInARow) +
                        "\n" +
                        "${bold("${EStats.MAX_LUCKY_IN_A_ROW.description}:")} " +
                        "${stats.maxLuckyInARow}"
            EAchievements.AVENUE ->
                return achievementProgressTextHelper(
                    achievement,
                    statValue = stats.smallStraight + stats.bigStraight
                )
            EAchievements.LOW_STAKES ->
                return "${bold(EDiceCombos.HIGH_CARD.publicName)}: ${stats.highCard}\n" +
                        "${bold(EDiceCombos.PAIR.publicName)}: ${stats.pair}\n" +
                        "${bold(EDiceCombos.TWO_PAIR.publicName)}: ${stats.twoPair}\n" +
                        "${bold(EDiceCombos.TRIPLE.publicName)}: ${stats.triple}\n" +
                        achievementProgressTextHelper(
                            achievement,
                            statValue = stats.highCard + stats.pair + stats.twoPair + stats.triple
                        )
            EAchievements.WHITE_SET ->
                return achievementProgressTextHelper(achievement, stats.whiteSetPlayed)
            EAchievements.BOOK_COVER ->
                return achievementProgressTextHelper(
                    achievement,
                    statValue = stats.handsPlayed - stats.whiteSetPlayed
                )
            EAchievements.HANDY ->
                return achievementProgressTextHelper(achievement, stats.handsPlayed)
            EAchievements.BIG_LEAGUES ->
                return achievementProgressTextHelper(achievement, stats.highestScore)
            EAchievements.MONEY_MAKER ->
                return achievementProgressTextHelper(achievement, stats.currentMoney)
            EAchievements.KING ->
                return achievementProgressTextHelper(achievement, stats.poker)
            else -> return ""
        }
    }

    private fun achievementProgressTextHelper(
        achievement: EAchievements,
        statValue: Long
    ): String {
        return "${bold("${achievement.resultText}:")} $statValue"
    }

    private fun totalAchievementCountHelper(user: UserEntity): String {
        return emojiWrapper(
            127941, bold(
                "Всего получено достижений: " +
                        "${countUserAchievements(user)}"
            )
        )
    }

    private fun newGamePlusTextHelper(stats: StatsEntity): String {
        return if (stats.newGamePlus > 0) {
            emojiWrapper(128520, bold("Новая игра плюс: ${stats.newGamePlus}")) +
                    "\n"
        } else {
            ""
        }
    }

    private fun achievementEmojiHelper(
        user: UserEntity,
        achievement: AchievementEntity,
        newGamePlus: Long
    ): String {
        return if (checkUserHasAchievement(user, achievement, newGamePlus)) {
            emoji(9989)
        } else {
            emoji(10060)
        }
    }

}