package com.sageone.dicepokerbot.database.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "achievements")
class AchievementsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", nullable = false)
    var userId: UserEntity? = null

    @Column(name = "snake_eyes", nullable = false)
    var snakeEyes: Boolean? = null

    @Column(name = "pear", nullable = false)
    var pear: Boolean? = null

    @Column(name = "avenue", nullable = false)
    var avenue: Boolean? = null

    @Column(name = "lucky_number", nullable = false)
    var luckyNumber: Boolean? = null

    @Column(name = "low_stakes", nullable = false)
    var lowStakes: Boolean? = null

    @Column(name = "white_set", nullable = false)
    var whiteSet: Boolean? = null

    @Column(name = "black_set", nullable = false)
    var blackSet: Boolean? = null

    @Column(name = "big_money", nullable = false)
    var bigMoney: Boolean? = null

    @Column(name = "bigger_money", nullable = false)
    var biggerMoney: Boolean? = null

    @Column(name = "millionaire", nullable = false)
    var millionaire: Boolean? = null

    @Column(name = "king", nullable = false)
    var king: Boolean? = null

    @Column(name = "alpha", nullable = false)
    var alpha: Boolean? = null

    @Column(name = "dice_poker", nullable = false)
    var dicePoker: Boolean? = null

}