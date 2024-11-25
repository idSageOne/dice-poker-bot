package com.sageone.dicepokerbot.database.jpa.repositories

import com.sageone.dicepokerbot.database.jpa.entity.ChatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : JpaRepository<ChatEntity, Long> {
    fun countByTelegramId(telegramId: Long): Int
    fun findByTelegramId(telegramId: Long): ChatEntity?
}