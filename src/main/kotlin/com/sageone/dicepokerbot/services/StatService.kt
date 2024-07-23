package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.EDiceCombos
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

    fun changeAndUpdateStats(
        stats: StatsEntity,
        handType: EDiceCombos,
        points: Long,
        money: Long,
        diceSet: String?
    ) {
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
        stats.handsPlayed++
        when (diceSet) {
            "white" -> stats.handsPlayedWhiteSet++
            "black" -> stats.handsPlayedBlackSet++
        }
        stats.pointsEarned += points
        stats.moneyEarned += money
        if (stats.highestScore < points) {
            stats.highestScore = points
        }
        updateStats(stats)
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