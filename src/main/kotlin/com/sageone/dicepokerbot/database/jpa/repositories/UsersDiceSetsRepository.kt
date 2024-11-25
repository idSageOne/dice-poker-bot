package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.DiceSetEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersDiceSetsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersDiceSetsRepository : JpaRepository<UsersDiceSetsEntity, Long> {
    fun countByUserIdAndAvailableSetId(user: UserEntity, diceSet: DiceSetEntity): Int
    fun findAllByUserId(user: UserEntity): List<UsersDiceSetsEntity>
    fun findByUserIdAndEnabledTrue(user: UserEntity): UsersDiceSetsEntity
}