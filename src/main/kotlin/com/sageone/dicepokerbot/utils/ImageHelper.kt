package com.sageone.dicepokerbot.utils

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun randomColor(chance: Int = Randomizer().getDiceRoll(16)) : Color {
    return when (chance) {
        1 -> Color(242, 111, 100, 255)
        2 -> Color(110, 23, 25, 255)
        3 -> Color(243, 99, 36, 255)
        4 -> Color(252, 176, 62, 255)
        5 -> Color (248, 204, 71, 255)
        6 -> Color(171, 151, 127, 255)
        7 -> Color(207, 186, 159, 255)
        8 -> Color(29, 55, 54, 255)
        9 -> Color(149, 187, 144, 255)
        10 -> Color(152, 183, 168, 255)
        11 -> Color (37, 138, 146, 255)
        12 -> Color (61, 138, 190, 255)
        13 -> Color (87, 104, 121, 255)
        14 -> Color(44, 0, 75, 255)
        15 -> Color(86, 77, 102, 255)
        else -> Color(58, 58, 61, 255)
    }
}

fun generatePokerImage(diceValues: IntArray, diceSet: String): File {
    // Полноразмерный фон в буфере
    val background = BufferedImage(660, 200, BufferedImage.TYPE_INT_ARGB)
    val aa: Graphics2D = background.createGraphics()
    aa.color = randomColor()
    aa.fillRect(0, 0, background.width, background.height)
    aa.dispose()

    // Массив с изображениями кубиков в буфере
    val diceImagePath = ".\\src\\main\\resources\\static\\$diceSet\\"
    val diceImages = mutableListOf<BufferedImage>()
    for (i in 0..4) {
        diceImages += ImageIO.read(File("${diceImagePath}${diceValues[i]}.png"))
    }

    // Новая картинка в буфере, в которую запишем результат склейки
    val resultImage = BufferedImage(background.width, background.height, BufferedImage.TYPE_INT_ARGB)
    val rr = resultImage.graphics
    rr.drawImage(background, 0, 0, null)
    var xAxis: Int
    for (i in 0..4) {
        if (i > 0) {
            xAxis = 120
            rr.drawImage(diceImages[i], xAxis * i + 40, 50, null)
        }
        else {
            xAxis = 40
            rr.drawImage(diceImages[i], xAxis, 50, null)
        }
    }
    rr.dispose()

    // Выводим результат в файл
    val output = File(".\\src\\main\\resources\\static\\0.png")
    ImageIO.write(resultImage, "png", output)
    return output
}

fun generateDiceSetImage(diceSetsMap: MutableMap<String, String>): File {
    // Полноразмерный фон в буфере
    val background = BufferedImage(660, 200, BufferedImage.TYPE_INT_ARGB)
    val aa: Graphics2D = background.createGraphics()
    aa.color = randomColor()
    aa.fillRect(0, 0, background.width, background.height)
    aa.dispose()

    // Массив с изображениями кубиков в буфере
    val diceImages = mutableListOf<BufferedImage>()
    val diceValue = Randomizer().getDiceRoll(6)
    var diceImagePath: String
    for (i in diceSetsMap) {
        diceImagePath = ".\\src\\main\\resources\\static\\${i.key}\\"
        diceImages += ImageIO.read(File("${diceImagePath}${diceValue}.png"))
    }

    // Новая картинка в буфере, в которую запишем результат склейки
    val resultImage = BufferedImage(background.width, background.height, BufferedImage.TYPE_INT_ARGB)
    val rr = resultImage.graphics
    rr.drawImage(background, 0, 0, null)
    val imagesCount = diceImages.size
    val xAxisIterator: Int = 660 / (imagesCount + 1)
    for (i in 0 until imagesCount) {
            rr.drawImage(diceImages[i], xAxisIterator * (i + 1)  - 49, 50, null)
    }
    rr.dispose()

    // Выводим результат в файл
    val output = File(".\\src\\main\\resources\\static\\1.png")
    ImageIO.write(resultImage, "png", output)
    return output
}