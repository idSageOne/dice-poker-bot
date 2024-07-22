package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.DiceSetEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersAvailableDiceSetsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersAvailableDiceSetsRepository : JpaRepository<UsersAvailableDiceSetsEntity, Long> {
    fun countByUserIdAndAvailableSetId(user: UserEntity, diceSet: DiceSetEntity) : Int
    fun findAllByUserId(user: UserEntity) : List<UsersAvailableDiceSetsEntity>
}