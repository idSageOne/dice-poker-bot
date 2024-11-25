package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.DiceSetEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersDiceSetsEntity
import com.sageone.dicepokerbot.database.jpa.repositories.DiceSetRepository
import com.sageone.dicepokerbot.database.jpa.repositories.UsersDiceSetsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DiceSetService {

    @Autowired
    lateinit var diceSetRepository: DiceSetRepository

    @Autowired
    lateinit var usersDiceSetsRepository: UsersDiceSetsRepository

    // Связка пользователей и наборов кубиков
    fun linkDiceSetToUser(
        user: UserEntity,
        diceSetSystemName: String,
        autoEnable: Boolean
    ) {
        val diceSet = diceSetRepository.findBySystemName(diceSetSystemName)
        val linkedDiceSet = UsersDiceSetsEntity()
        if (!checkUserHasDiceSet(user, diceSet)) {
            linkedDiceSet.userId = user
            linkedDiceSet.availableSetId = diceSet
            if (autoEnable) {
                linkedDiceSet.enabled = true
                disableUserDiceSets(user)
            } else {
                linkedDiceSet.enabled = false
            }
            usersDiceSetsRepository.save(linkedDiceSet)
            println("linkDiceSetToUser: ${linkedDiceSet.availableSetId!!.systemName} $autoEnable")
        }
    }

    fun createShopDiceSetsMap(user: UserEntity): MutableMap<String, String> {
        val ownedDiceSets = createOwnedDiceSetMap(user)
        val diceSetMap = createDiceSetMap()
        for (i in ownedDiceSets) {
            diceSetMap.remove(i.key)
        }
        println("createShopDiceSetsMap: $diceSetMap")
        return diceSetMap
    }

    fun createOwnedDiceSetMap(user: UserEntity): MutableMap<String, String> {
        val diceSetMap = mutableMapOf<String, String>()
        val userDiceSets = usersDiceSetsRepository.findAllByUserId(user)
        for (i in userDiceSets) {
            diceSetMap[i.availableSetId!!.systemName] = i.availableSetId!!.publicName
        }
        println("createOwnedDiceSetMap: $diceSetMap")
        return diceSetMap
    }

    fun createDiceSetMap(): MutableMap<String, String> {
        val diceSetMap = mutableMapOf<String, String>()
        val diceSets = readDiceSets()
        for (i in diceSets) {
            diceSetMap[i.systemName] = i.publicName
        }
        println("createDiceSetMap: $diceSetMap")
        return diceSetMap
    }

    fun readUserEnabledDiceSet(user: UserEntity): DiceSetEntity {
        return usersDiceSetsRepository.findByUserIdAndEnabledTrue(user).availableSetId!!
    }

    fun enableUserDiceSet(user: UserEntity, diceSet: DiceSetEntity) {
        disableUserDiceSets(user)
        val linkedDiceSet = UsersDiceSetsEntity()
        linkedDiceSet.userId = user
        linkedDiceSet.availableSetId = diceSet
        linkedDiceSet.enabled = true
        usersDiceSetsRepository.save(linkedDiceSet)
    }

    fun readDiceSets(): MutableList<DiceSetEntity> {
        return diceSetRepository.findAllByOrderByCostAsc()
    }

    fun readDiceSetCost(systemName: String): Long {
        return diceSetRepository.findBySystemName(systemName).cost
    }

    fun checkUserHasDiceSet(user: UserEntity, diceSet: DiceSetEntity): Boolean {
        return usersDiceSetsRepository.countByUserIdAndAvailableSetId(user, diceSet) > 0
    }

    private fun disableUserDiceSets(user: UserEntity) {
        val userDiceSets = usersDiceSetsRepository.findAllByUserId(user)
        for (i in userDiceSets) {
            if (i.enabled) {
                i.enabled = false
                usersDiceSetsRepository.save(i)
                println("disableUserDiceSets: ${i.availableSetId!!.systemName} отключен")
            }
        }
    }
}
