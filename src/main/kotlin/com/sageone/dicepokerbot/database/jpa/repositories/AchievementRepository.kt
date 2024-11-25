package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.AchievementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AchievementRepository : JpaRepository<AchievementEntity, Long> {
    fun findBySystemName(systemName: String): AchievementEntity
    fun findAllBySystemName(systemName: String): MutableList<AchievementEntity>
}