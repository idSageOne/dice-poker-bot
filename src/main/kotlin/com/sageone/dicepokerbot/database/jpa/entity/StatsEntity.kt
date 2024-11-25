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

    // User profile stats

    @Column(name = "last_combo", nullable = false)
    var lastCombo: String = ""

    @Column(name = "current_money", nullable = false)
    var currentMoney: Long = 0

    @Column(name = "max_money", nullable = false)
    var maxMoney: Long = 0

    @Column(name = "points", nullable = false)
    var points: Long = 0

    @Column(name = "tokens", nullable = false)
    var tokens: Long = 0

    @Column(name = "new_game_plus", nullable = false)
    var newGamePlus: Long = 0

    // Quest stats

    @Column(name = "quests_completed", nullable = false)
    var questsCompleted: Long = 0

    @Column(name = "common_quests", nullable = false)
    var commonQuests: Long = 0

    @Column(name = "rare_quests", nullable = false)
    var rareQuests: Long = 0

    @Column(name = "epic_quests", nullable = false)
    var epicQuests: Long = 0

    @Column(name = "legendary_quests", nullable = false)
    var legendaryQuests: Long = 0

    // Combo stats

    @Column(name = "high_card", nullable = false)
    var highCard: Long = 0

    @Column(name = "pair", nullable = false)
    var pair: Long = 0

    @Column(name = "two_pair", nullable = false)
    var twoPair: Long = 0

    @Column(name = "triple", nullable = false)
    var triple: Long = 0

    @Column(name = "full_house", nullable = false)
    var fullHouse: Long = 0

    @Column(name = "small_straight", nullable = false)
    var smallStraight: Long = 0

    @Column(name = "big_straight", nullable = false)
    var bigStraight: Long = 0

    @Column(name = "quad", nullable = false)
    var quad: Long = 0

    @Column(name = "poker", nullable = false)
    var poker: Long = 0

    // Dice stats

    @Column(name = "one_played", nullable = false)
    var onePlayed: Long = 0

    @Column(name = "two_played", nullable = false)
    var twoPlayed: Long = 0

    @Column(name = "three_played", nullable = false)
    var threePlayed: Long = 0

    @Column(name = "four_played", nullable = false)
    var fourPlayed: Long = 0

    @Column(name = "five_played", nullable = false)
    var fivePlayed: Long = 0

    @Column(name = "six_played", nullable = false)
    var sixPlayed: Long = 0

    // Other stats

    @Column(name = "hands_played", nullable = false)
    var handsPlayed: Long = 0

    @Column(name = "white_set_played", nullable = false)
    var whiteSetPlayed: Long = 0

    @Column(name = "pairs_in_a_row", nullable = false)
    var pairsInARow: Long = 0

    @Column(name = "max_pairs_in_a_row", nullable = false)
    var maxPairsInARow: Long = 0

    @Column(name = "lucky_in_a_row", nullable = false)
    var luckyInARow: Long = 0

    @Column(name = "unlucky_in_a_row", nullable = false)
    var unluckyInARow: Long = 0

    @Column(name = "max_lucky_in_a_row", nullable = false)
    var maxLuckyInARow: Long = 0

    @Column(name = "highest_score", nullable = false)
    var highestScore: Long = 0
}