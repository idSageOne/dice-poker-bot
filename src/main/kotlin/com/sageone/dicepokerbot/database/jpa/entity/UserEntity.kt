package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @Column(name = "telegram_id", nullable = false, unique = true)
    var telegramId: Long? = null

    @Column(name = "telegram_handle")
    var telegramHandle: String? = null

    @Column(name = "is_bot", nullable = false)
    var isBot: Boolean? = null

    @Column(name = "is_alpha_tester", nullable = false)
    var isAlphaTester: Boolean = false

    @Column(name = "is_beta_tester", nullable = false)
    var isBetaTester: Boolean = false

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}