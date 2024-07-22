package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.AchievementsEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AchievementsRepository : JpaRepository<AchievementsEntity, Long> {
    fun findByUserId(user: UserEntity) : AchievementsEntity
    fun countByUserId(user: UserEntity) : Int
}