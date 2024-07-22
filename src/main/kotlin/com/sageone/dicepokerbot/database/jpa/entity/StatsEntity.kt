package com.sageone.dicepokerbot.database.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "stats")
class StatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", nullable = false)
    var userId: UserEntity? = null

    @Column(name = "high_card", nullable = false)
    var highCard: Long? = 0

    @Column(name = "pair", nullable = false)
    var pair: Long? = 0

    @Column(name = "two_pair", nullable = false)
    var twoPair: Long? = 0

    @Column(name = "triple", nullable = false)
    var triple: Long? = 0

    @Column(name = "full_house", nullable = false)
    var fullHouse: Long? = 0

    @Column(name = "small_straight", nullable = false)
    var smallStraight: Long? = 0

    @Column(name = "big_straight", nullable = false)
    var bigStraight: Long? = 0

    @Column(name = "quad", nullable = false)
    var quad: Long? = 0

    @Column(name = "poker", nullable = false)
    var poker: Long? = 0

    @Column(name = "hands_played", nullable = false)
    var handsPlayed: Long? = 0

    @Column(name = "hands_played_white_set", nullable = false)
    var handsPlayedWhiteSet: Long? = 0

    @Column(name = "hands_played_black_set", nullable = false)
    var handsPlayedBlackSet: Long? = 0

    @Column(name = "pairs_in_a_row", nullable = false)
    var pairsInARow: Long? = 0

    @Column(name = "points_earned", nullable = false)
    var pointsEarned: Long? = 0

    @Column(name = "money_earned", nullable = false)
    var moneyEarned: Long? = 0

    @Column(name = "highest_score", nullable = false)
    var highestScore: Long? = 0
}