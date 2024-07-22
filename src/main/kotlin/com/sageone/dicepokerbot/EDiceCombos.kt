package com.sageone.dicepokerbot

enum class EDiceCombos(
    var systemName: String,
    var publicName: String,
    var comboText: String,
    var basePoints: Int
)
{

    HIGH_CARD(
        systemName = "highCard",
        publicName = "Старшая карта",
        comboText = "Старшая карта:",
        basePoints = 0),
    PAIR(
        systemName = "pair",
        publicName = "Пара",
        comboText = "Пара:",
        basePoints = 100),
    TWO_PAIR(
        systemName = "twoPair",
        publicName = "Две пары",
        comboText = "Две пары:",
        basePoints = 300),
    TRIPLE(
        systemName = "triple",
        publicName = "Три в ряд",
        comboText = "Три в ряд:",
        basePoints = 600),
    FULL_HOUSE(
        systemName = "fullHouse",
        publicName = "Фулл хаус",
        comboText = "Фулл хаус:",
        basePoints = 800),
    SMALL_STRAIGHT(
        systemName = "smallStraight",
        publicName = "Малый стрит",
        comboText = "Малый стрит!",
        basePoints = 1500),
    BIG_STRAIGHT(
        systemName = "bigStraight",
        publicName = "Большой стрит",
        comboText = "Большой стрит!",
        basePoints = 1900),
    QUAD(
        systemName = "quad",
        publicName = "Четыре в ряд",
        comboText = "Четыре в ряд!!",
        basePoints = 2200),
    POKER(
        systemName = "poker",
        publicName = "Покер",
        comboText = "Покер!!!",
        basePoints = 9000);

    fun comboText() = "$publicName:"

    val text : String
    get() = ""
}