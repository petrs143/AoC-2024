package day03

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/day03/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

fun part1(input: List<String>): String {
    val text = buildString {
        input.forEach { append(it) }
    }
    return sumMuls(text).toString()
}

private fun sumMuls(text: String): Long {
    val regex = Regex("""mul\([0-9]+,[0-9]+\)""")
    return regex.findAll(text)
        .sumOf {
            it.value.removePrefix("mul(")
                .removeSuffix(")")
                .split(",")
                .let { it[0].toLong() * it[1].toLong() }
        }
}

fun part2(input: List<String>): String {
    val text = buildString {
        input.forEach { append(it) }
    }

    return text.split("do")
        .filterNot { it.startsWith("n't") }
        .sumOf { sumMuls(it) }
        .toString()
}