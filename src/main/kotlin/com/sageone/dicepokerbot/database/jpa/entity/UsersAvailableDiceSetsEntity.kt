package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users_available_dice_sets")
class UsersAvailableDiceSetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", nullable = false)
    var userId: UserEntity? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "available_set_id", nullable = false)
    var availableSetId: DiceSetEntity? = null

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}