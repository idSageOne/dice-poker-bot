package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.EDiceCombos
import com.sageone.dicepokerbot.EStats
import com.sageone.dicepokerbot.database.jpa.entity.StatsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.repositories.StatsRepository
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

    fun statsToMap(stats: StatsEntity): MutableMap<EStats, Long> {
        return mutableMapOf(
            EStats.POKER to stats.poker!!,
            EStats.QUAD to stats.quad!!,
            EStats.FULL_HOUSE to stats.fullHouse!!,
            EStats.TRIPLE to stats.triple!!,
            EStats.TWO_PAIR to stats.twoPair!!,
            EStats.PAIR to stats.pair!!,
            EStats.SMALL_STRAIGHT to stats.smallStraight!!,
            EStats.BIG_STRAIGHT to stats.bigStraight!!,
            EStats.HIGH_CARD to stats.highCard!!,
            EStats.HANDS_PLAYED to stats.handsPlayed!!,
            EStats.WHITE_SET to stats.handsPlayedWhiteSet!!,
            EStats.BLACK_SET to stats.handsPlayedBlackSet!!,
            EStats.PAIRS_IN_A_ROW to stats.pairsInARow!!,
            EStats.POINTS_EARNED to stats.pointsEarned!!,
            EStats.MONEY_EARNED to stats.moneyEarned!!,
            EStats.HIGHEST_SCORE to stats.highestScore!!
        )
    }

    fun statsToEntityAndUpdate(stats: StatsEntity, statsMap: MutableMap<EStats, Long>) {
        stats.poker = statsMap[EStats.POKER]!!
        stats.quad = statsMap[EStats.QUAD]!!
        stats.fullHouse = statsMap[EStats.FULL_HOUSE]!!
        stats.triple = statsMap[EStats.TRIPLE]!!
        stats.twoPair = statsMap[EStats.TWO_PAIR]!!
        stats.pair = statsMap[EStats.PAIR]!!
        stats.smallStraight = statsMap[EStats.SMALL_STRAIGHT]!!
        stats.bigStraight = statsMap[EStats.BIG_STRAIGHT]!!
        stats.highCard = statsMap[EStats.HIGH_CARD]!!
        stats.handsPlayed = statsMap[EStats.HANDS_PLAYED]!!
        stats.handsPlayedWhiteSet = statsMap[EStats.WHITE_SET]!!
        stats.handsPlayedBlackSet = statsMap[EStats.BLACK_SET]!!
        stats.pairsInARow = statsMap[EStats.PAIRS_IN_A_ROW]!!
        stats.pointsEarned = statsMap[EStats.POINTS_EARNED]!!
        stats.moneyEarned = statsMap[EStats.MONEY_EARNED]!!
        stats.highestScore = statsMap[EStats.HIGHEST_SCORE]!!
        updateStats(stats)
    }

    fun analyseStats(
        statsMap: MutableMap<EStats, Long>,
        handType: EDiceCombos,
        points: Long,
        money: Long,
        diceSet: String?
    ) {
        when (handType) {
            EDiceCombos.POKER -> {
                statsMap.merge(EStats.POKER, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.QUAD -> {
                statsMap.merge(EStats.QUAD, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.FULL_HOUSE -> {
                statsMap.merge(EStats.FULL_HOUSE, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.TRIPLE -> {
                statsMap.merge(EStats.TRIPLE, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.TWO_PAIR -> {
                statsMap.merge(EStats.TWO_PAIR, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.PAIR -> {
                statsMap.merge(EStats.PAIR, 1, Long::plus)
                statsMap.merge(EStats.PAIRS_IN_A_ROW, 1, Long::plus)
            }
            EDiceCombos.SMALL_STRAIGHT -> {
                statsMap.merge(EStats.SMALL_STRAIGHT, 1, Long::plus)
                statsMap[EStats.PAIRS_IN_A_ROW] = 0
            }
            EDiceCombos.BIG_STRAIGHT -> {
                statsMap.merge(EStats.BIG_STRAIGHT, 1, Long::plus)
                statsMap[EStats.PAIRS_IN_A_ROW] = 0
            }
            else -> {
                statsMap.merge(EStats.HIGH_CARD, 1, Long::plus)
                statsMap[EStats.PAIRS_IN_A_ROW] = 0
            }
        }
        statsMap.merge(EStats.HANDS_PLAYED, 1, Long::plus)
        when (diceSet) {
            "white" -> statsMap.merge(EStats.WHITE_SET, 1, Long::plus)
            "black" -> statsMap.merge(EStats.BLACK_SET, 1, Long::plus)
        }
        statsMap.merge(EStats.POINTS_EARNED, points, Long::plus)
        statsMap.merge(EStats.MONEY_EARNED, money, Long::plus)
        if (statsMap[EStats.HIGHEST_SCORE]!! < points) {
            statsMap[EStats.HIGHEST_SCORE] = points
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
        return statsRepository.findTop5ByOrderByPointsEarnedDesc()
    }

    fun readTopMoney(): List<StatsEntity>? {
        return statsRepository.findTop5ByOrderByMoneyEarnedDesc()
    }

    fun readUserStats(user: UserEntity): StatsEntity {
        return statsRepository.findByUserId(user)
    }

    fun checkUserHasStats(user: UserEntity): Boolean {
        return statsRepository.countByUserId(user) > 0
    }

}