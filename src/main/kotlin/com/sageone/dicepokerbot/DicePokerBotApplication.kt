package com.sageone.dicepokerbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime

@SpringBootApplication
class DicePokerBotApplication

lateinit var contextStartDate: LocalDateTime
lateinit var botStartDate: LocalDateTime

fun main(args: Array<String>) {
    contextStartDate = LocalDateTime.now()
    runApplication<DicePokerBotApplication>(*args)
    botStartDate = LocalDateTime.now()
}