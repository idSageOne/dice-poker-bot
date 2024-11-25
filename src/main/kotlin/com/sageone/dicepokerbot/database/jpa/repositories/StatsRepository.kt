package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.StatsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatsRepository : JpaRepository<StatsEntity, Long> {
    fun findByUserId(user: UserEntity): StatsEntity
    fun countByUserId(user: UserEntity): Int
    fun findTop5ByOrderByPointsDesc(): List<StatsEntity>
    fun findTop5ByOrderByCurrentMoneyDesc(): List<StatsEntity>
}