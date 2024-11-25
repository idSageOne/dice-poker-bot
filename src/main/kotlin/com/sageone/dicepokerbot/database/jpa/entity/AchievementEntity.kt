package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "achievements")
class AchievementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @Column(name = "system_name", nullable = false)
    var systemName: String = ""

    @Column(name = "public_name", nullable = false)
    var publicName: String = ""

    @Column(name = "requirement", nullable = false)
    var requirement: Long = 0

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}