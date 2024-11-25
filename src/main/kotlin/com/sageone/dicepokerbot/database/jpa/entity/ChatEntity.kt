package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "chats")
class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @Column(name = "telegram_id", nullable = false, unique = true)
    var telegramId: Long? = null

    @Column(name = "is_group_chat", nullable = false)
    var isGroupChat: Boolean = false

    @Column(name = "is_user_chat", nullable = false)
    var isUserChat: Boolean = false

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}