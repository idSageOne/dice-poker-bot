package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.DiceSetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DiceSetRepository : JpaRepository<DiceSetEntity, Long> {
    fun findDiceSetsEntityBySystemName(systemName: String) : DiceSetEntity
    fun findAllByOrderByCostAsc() : MutableList<DiceSetEntity>
    fun findAllByIdIsNotIn(diceSet: List<DiceSetEntity>) : List<DiceSetEntity>
    fun findById(diceSet: DiceSetEntity) : DiceSetEntity
}