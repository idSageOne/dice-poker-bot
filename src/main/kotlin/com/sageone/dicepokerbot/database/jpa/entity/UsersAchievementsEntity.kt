package com.sageone.dicepokerbot.database.jpa.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users_achievements")
class UsersAchievementsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", nullable = false)
    var userId: UserEntity? = null

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "achievement_id", nullable = false)
    var achievementId: AchievementEntity? = null

    @Column(name = "new_game_plus", nullable = false)
    var newGamePlus: Long = 0

    @Column(name = "date_created", insertable = false)
    var dateCreated: Date? = null

}