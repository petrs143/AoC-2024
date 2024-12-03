package day02

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/day02/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

fun part1(input: List<String>): String {
    return input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }
        isSafe(numbers)
    }.toString()
}

private fun isSafe(numbers: List<Int>): Boolean {
    val isSorted = numbers.sorted() == numbers || numbers.sortedDescending() == numbers
    val withinLevels = numbers.zipWithNext().all { (first, second) ->
        abs(first - second) in (1..3)
    }

    return isSorted && withinLevels
}

fun part2(input: List<String>): String {
    return input.count { line ->
        val numbers = line.split(" ").map { it.toInt() }

        isSafe(numbers) || numbers.mapIndexed { index, _ ->
            numbers.toMutableList().also {
                it.removeAt(index)
            }
        }.any { isSafe(it) }
    }.toString()
}