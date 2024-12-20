package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "dice_sets")
class DiceSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @Column(name = "system_name", nullable = false, unique = true)
    var systemName: String = ""

    @Column(name = "public_name", nullable = false, unique = true)
    var publicName: String = ""

    @Column(name = "cost", nullable = false)
    var cost: Long = 0

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}