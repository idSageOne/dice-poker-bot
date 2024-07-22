package com.sageone.dicepokerbot.services

import com.sageone.dicepokerbot.database.jpa.entity.DiceSetEntity
import com.sageone.dicepokerbot.database.jpa.entity.UserEntity
import com.sageone.dicepokerbot.database.jpa.entity.UsersAvailableDiceSetsEntity
import com.sageone.dicepokerbot.database.jpa.repositories.DiceSetRepository
import com.sageone.dicepokerbot.database.jpa.repositories.UserRepository
import com.sageone.dicepokerbot.database.jpa.repositories.UsersAvailableDiceSetsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var diceSetRepository: DiceSetRepository

    @Autowired
    lateinit var diceSetsToUsersRepository: UsersAvailableDiceSetsRepository

    fun createUser(message: Message): UserEntity {
        val telegramId = message.from.id
        var user = UserEntity()
        if (!checkUserExists(telegramId)) {
            user.telegramId = message.from.id
            user.telegramHandle = message.from.userName
            user.isBot = message.from.isBot
            user.enabledDiceSet = diceSetRepository.findDiceSetsEntityBySystemName("white")
            user = userRepository.save(user)
        } else {
            user = userRepository.findByTelegramId(telegramId)!!
        }
        linkDiceSet(user, listOf("white"))
        return user
    }

    // Связка пользователей и наборов кубиков
    fun linkDiceSet(user: UserEntity, diceSetSystemName: List<String>) {
        var diceSet: DiceSetEntity
        for (i in diceSetSystemName) {
            diceSet = diceSetRepository.findDiceSetsEntityBySystemName(i)
            if (!checkUserHasDiceSet(user, diceSet)) {
                val linkedDiceSet = UsersAvailableDiceSetsEntity()
                linkedDiceSet.userId = user
                linkedDiceSet.availableSetId = diceSet
                diceSetsToUsersRepository.save(linkedDiceSet)
            }
        }
    }

    fun readDiceSets(): MutableList<DiceSetEntity>{
        return diceSetRepository.findAllByOrderByCostAsc()
    }

    fun readDiceSetCost(systemName: String): Long {
        return diceSetRepository.findDiceSetsEntityBySystemName(systemName).cost!!
    }

    fun createShopDiceSetsMap(user: UserEntity): MutableMap<String, String> {
        val ownedDiceSets = createOwnedDiceSetMap(user)
        val diceSetMap = createDiceSetMap()
        for (i in ownedDiceSets) {
            diceSetMap.remove(i.key)
        }
//        println("createShopDiceSetsMap: $diceSetMap")
        return diceSetMap
    }

    fun createOwnedDiceSetMap(user: UserEntity): MutableMap<String, String> {
        val diceSetMap = mutableMapOf<String, String>()
        val userDiceSets = diceSetsToUsersRepository.findAllByUserId(user)
        for (i in userDiceSets) {
            diceSetMap[i.availableSetId!!.systemName!!] = i.availableSetId!!.publicName!!
        }
//        println("createOwnedDiceSetMap: $diceSetMap")
        return diceSetMap
    }

    fun createDiceSetMap(): MutableMap<String, String> {
        val diceSetMap = mutableMapOf<String, String>()
        val diceSets = readDiceSets()
        for (i in diceSets) {
            diceSetMap[i.systemName!!] = i.publicName!!
        }
//        println("createDiceSetMap: $diceSetMap")
        return diceSetMap
    }

    fun checkUserExists(telegramId: Long): Boolean {
        return userRepository.countByTelegramId(telegramId) > 0
    }

    fun checkUserHasDiceSet(user: UserEntity, diceSet: DiceSetEntity): Boolean {
        return diceSetsToUsersRepository.countByUserIdAndAvailableSetId(user, diceSet) > 0
    }

}