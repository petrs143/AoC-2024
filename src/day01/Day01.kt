package day01

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/day01/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

fun part1(input: List<String>): String {
    val parsedInput = input.map {
        it.split(" ")
            .let { it.first().toInt() to it.last().toInt() }
    }

    val firstList = parsedInput.map { it.first }
    val secondList = parsedInput.map { it.second }

    val firstSortedList = firstList.sorted()
    val secondSortedList = secondList.sorted()

    var sum = 0
    repeat(firstList.size) { index ->
        val firstNumber = firstSortedList[index]
        val secondNumber = secondSortedList[index]

        sum += abs(firstNumber - secondNumber)
    }

    return "$sum"
}

fun part2(input: List<String>): String {
    val parsedInput = input.map {
        it.split(" ")
            .let { it.first().toInt() to it.last().toInt() }
    }

    val firstList = parsedInput.map { it.first }
    val secondList = parsedInput.map { it.second }

    return firstList.sumOf { number ->
        val apperance = secondList.count { it == number }
        number * apperance
    }.toString()
}