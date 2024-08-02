package com.sageone.dicepokerbot

import com.sageone.dicepokerbot.enums.EDiceCombos
import com.sageone.dicepokerbot.enums.EStats
import com.sageone.dicepokerbot.utils.Randomizer
import com.sageone.dicepokerbot.utils.randomColor
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


@SpringBootTest
class ApplicationTests {

	@Test
	fun testPoints() {
		var dice: IntArray
		for (i in 1..10000) {
			dice = Randomizer().getDicePokerHand()
			Randomizer().getHandType(dice)
		}
	}

	@Test
	fun testImages() {

		// Полноразмерный фон в буфере
		val background = BufferedImage(660, 200, BufferedImage.TYPE_INT_ARGB)
		val aa: Graphics2D = background.createGraphics()
		aa.color = randomColor()
		aa.fillRect(0, 0, background.width, background.height)
		aa.dispose()

		// Массив с изображениями кубиков в буфере
		val diceImagePath = "D:\\Development\\DicePokerBot\\src\\main\\resources\\static\\white\\"
		val diceImages = mutableListOf<BufferedImage>()
		val dice = Randomizer().getDicePokerHand()
		for (i in 0..4) {
			diceImages += ImageIO.read(File(diceImagePath + "${dice[i]}.png"))
		}

		// Новая картинка в буфере, в которую запишем результат склейки
		val resultImage = BufferedImage(background.width, background.height, BufferedImage.TYPE_INT_ARGB)
		val rr = resultImage.graphics
		rr.drawImage(background, 0, 0, null)
		var xAxis: Int
		for (i in 0..4) {
			if (i > 0) {
				xAxis = 120
				rr.drawImage(diceImages[i], xAxis * i + 41, 51, null)
			}
			else {
				xAxis = 40
				rr.drawImage(diceImages[i], xAxis + 1, 51, null)
			}
		}
		rr.dispose()

		// Выводим результат в файл
		ImageIO.write(resultImage, "png", File("D:\\Development\\DicePokerBot\\src\\main\\resources\\static\\0.png"))
/*
		// Настраиваем скейлинг
		val scaleFactor = 1
		val width = c.width * scaleFactor
		val height = c.height * scaleFactor

		// Пустая новая картинка, в которую запишем результат
		val d = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

		// Записываем графику в пустую картинку и трансформируем пиксели алгоритмом nearest neighbor
		val g2: Graphics2D = d.createGraphics()
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
		g2.drawImage(c, 0, 0, width, height, null)
		g2.dispose()
*/
	}

	@Test
	fun moneyFormatter() {
		val cost = 1000000L
		val moneyFlipped = cost.toString().reversed()
		println(moneyFlipped)
		var result = ""
		for (i in moneyFlipped.indices) {
			result += moneyFlipped[i]
			if ((i + 1) % 3 == 0) { result += " " }
			println(result)
		}
		println(result.reversed())
	}

	@Test
	fun allCombos() {
		for (i in EStats.values()) {
			println(i.description)
		}
	}

	@Test
	fun countHandPoints() {
		val handValues = IntArray(5)
		handValues[0] = 6
		handValues[1] = 6
		handValues[2] = 6
		handValues[3] = 6
		handValues[4] = 6
		val handType = Randomizer().getHandType(handValues)
		println(handType)
		val diceCount = IntArray(6)
		for (i in 0..4) {
			diceCount[handValues[i] - 1]++
		}

		// Бонус за комбо
		val diceMult = 5
		val comboPoints = when (handType) {
			EDiceCombos.POKER -> (diceCount.indexOf(5) + 1) * 5
			EDiceCombos.QUAD -> (diceCount.indexOf(4) + 1) * 4
			EDiceCombos.FULL_HOUSE -> (diceCount.indexOf(3) + 1) * 3 + (diceCount.indexOf(2) + 1) * 2
			EDiceCombos.TRIPLE -> (diceCount.indexOf(3) + 1) * 3
			EDiceCombos.TWO_PAIR -> ((diceCount.indexOf(2) + 1) + (diceCount.lastIndexOf(2) + 1)) * 2
			EDiceCombos.PAIR -> (diceCount.indexOf(2) + 1) * 2
			else -> 0
		}

		// Бонус за каждый сыгранный кубик
		val handPoints = handValues.sum()

		// Итоговый бонус
		val result = handType.basePoints + (handPoints + comboPoints) * diceMult
	}

	@Test
	fun dicePoker() {
		var highCard = 0
		var pair = 0
		var twoPair = 0
		var triple = 0
		var quadruple = 0
		var poker = 0
		var fullHouse = 0
		var straight = 0
		val customRandom = Randomizer()

		for (a in 1..10000000) {
			val diceCount = IntArray(6)
			for (i in 0..4) {
				diceCount[customRandom.getDiceRoll(6) - 1]++
			}

			when (true) {
				diceCount.contains(5) -> poker++
				diceCount.contains(4) -> quadruple++
				diceCount.contains(3) && diceCount.contains(2) -> fullHouse++
				diceCount.contains(3) -> triple++
				diceCount.count { it == 2 } == 2 -> twoPair++
				diceCount.contains(2) -> pair++
				diceCount[0] == 0 || diceCount[5] == 0 -> straight++
				else -> highCard++
			}
		}

		val mm = hashMapOf<String, Int>()
		mm["high card"] = highCard // 0 points
		mm["pair"] = pair // 50 points
		mm["twoPair"] = twoPair // 100 points
		mm["triple"] = triple // 150 points
		mm["fullHouse"] = fullHouse // 300 points
		mm["straight"] = straight // 350 points
		mm["quadruple"] = quadruple // 500 points
		mm["poker"] = poker // 1000 points

		println("total rolls = ${highCard + pair + twoPair + triple + quadruple + fullHouse + straight + poker}")
		println("${mm.entries.sortedBy{it.value}}")

		//val result = mm.toList().sortedBy { (_, value) -> value}.toMap()
		//println("$result")
	}

}
