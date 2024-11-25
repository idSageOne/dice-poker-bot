package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.StatsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.repositories.StatsRepository
import com.sageone.dicepokerbot.enums.EDiceCombos
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class StatService {

    @Autowired
    lateinit var statsRepository: StatsRepository

    fun readOrCreateUserStats(user: UserEntity): StatsEntity {
        return if (checkUserHasStats(user))
            readUserStats(user)
        else createStats(user)
    }

    fun resetStatsForNewGamePlus(user: UserEntity) {
        val stats = readUserStats(user)
        stats.highCard = 0
        stats.pair = 0
        stats.twoPair = 0
        stats.triple = 0
        stats.smallStraight = 0
        stats.bigStraight = 0
        stats.fullHouse = 0
        stats.quad = 0
        stats.poker = 0
        stats.whiteSetPlayed = 0
        stats.handsPlayed = 0
        stats.highestScore = 0
        stats.newGamePlus++
        stats.tokens += 10 * stats.newGamePlus
        updateStats(stats)
    }

    fun changeAndUpdateStats(
        stats: StatsEntity,
        diceRoll: IntArray,
        handType: EDiceCombos,
        luckyRoll: Boolean,
        points: Long,
        money: Long,
        diceSet: String?
    ) {
        updateDiceStats(stats, diceRoll)
        updateComboStats(stats, handType)
        updateLuckyRollStats(stats, luckyRoll)
        updateLastCombo(stats, handType)
        stats.handsPlayed++
        stats.points += points
        stats.currentMoney += money
        stats.maxMoney += money
        if (diceSet == "white") {
            stats.whiteSetPlayed++
        }
        if (stats.highestScore < points) {
            stats.highestScore = points
        }
        updateStats(stats)
    }

    private fun updateComboStats(stats: StatsEntity, handType: EDiceCombos) {
        when (handType) {
            EDiceCombos.POKER -> {
                stats.poker++
                stats.pairsInARow++
            }
            EDiceCombos.QUAD -> {
                stats.quad++
                stats.pairsInARow++
            }
            EDiceCombos.FULL_HOUSE -> {
                stats.fullHouse++
                stats.pairsInARow++
            }
            EDiceCombos.TRIPLE -> {
                stats.triple++
                stats.pairsInARow++
            }
            EDiceCombos.TWO_PAIR -> {
                stats.twoPair++
                stats.pairsInARow++
            }
            EDiceCombos.PAIR -> {
                stats.pair++
                stats.pairsInARow++
            }
            EDiceCombos.SMALL_STRAIGHT -> {
                stats.smallStraight++
                stats.pairsInARow = 0
            }
            EDiceCombos.BIG_STRAIGHT -> {
                stats.bigStraight++
                stats.pairsInARow = 0
            }
            else -> {
                stats.highCard++
                stats.pairsInARow = 0
            }
        }
        if (stats.pairsInARow > stats.maxPairsInARow) {
            stats.maxPairsInARow = stats.pairsInARow
        }
    }

    private fun updateDiceStats(stats: StatsEntity, diceRoll: IntArray) {
        for (i in diceRoll) {
            when (i) {
                1 -> stats.onePlayed++
                2 -> stats.twoPlayed++
                3 -> stats.threePlayed++
                4 -> stats.fourPlayed++
                5 -> stats.fivePlayed++
                6 -> stats.sixPlayed++
            }
        }
    }

    private fun updateLuckyRollStats(stats: StatsEntity, luckyRoll: Boolean) {
        if (luckyRoll) {
            stats.luckyInARow++
            stats.unluckyInARow = 0
            if (stats.luckyInARow > stats.maxLuckyInARow) {
                stats.maxLuckyInARow = stats.luckyInARow
            }
        } else {
            stats.luckyInARow = 0
            stats.unluckyInARow++
        }
    }

    private fun updateLastCombo(stats: StatsEntity, handType: EDiceCombos) {
        if ((stats.lastCombo == "smallStraight" || stats.lastCombo == "bigStraight") && handType.systemName == "fullHouse") {
            stats.lastCombo = "homeRun"
        } else {
            stats.lastCombo = handType.systemName
        }
    }

    fun createStats(user: UserEntity): StatsEntity {
        val stats = StatsEntity()
        stats.userId = user
        statsRepository.save(stats)
        return stats
    }

    fun updateStats(stats: StatsEntity) {
        statsRepository.save(stats)
    }

    fun readTopPoints(): List<StatsEntity>? {
        return statsRepository.findTop5ByOrderByPointsDesc()
    }

    fun readTopMoney(): List<StatsEntity>? {
        return statsRepository.findTop5ByOrderByCurrentMoneyDesc()
    }

    fun readUserStats(user: UserEntity): StatsEntity {
        return statsRepository.findByUserId(user)
    }

    fun checkUserHasStats(user: UserEntity): Boolean {
        return statsRepository.countByUserId(user) > 0
    }

}