package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.AchievementEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersAchievementsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersAchievementsRepository : JpaRepository<UsersAchievementsEntity, Long> {
    fun findAllByUserId(user: UserEntity): MutableList<UsersAchievementsEntity>
    fun findAllByUserIdAndAndNewGamePlus(user: UserEntity, newGamePlus: Long): MutableList<UsersAchievementsEntity>
    fun countByUserId(user: UserEntity): Int
    fun countByUserIdAndAchievementIdAndNewGamePlus(
        user: UserEntity,
        achievement: AchievementEntity,
        newGamePlus: Long
    ): Int
}