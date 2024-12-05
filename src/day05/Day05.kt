package day05

import java.io.File

fun main() {
    val input = File("src/day05/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

fun part1(input: List<String>): String {
    val emptyLineIndex = input.indexOfFirst { it.isBlank() }
    val rules = input.subList(0, emptyLineIndex)
        .map {
            it.split("|").let {
                it[0].toInt() to it[1].toInt()
            }
        }.groupBy { it.first }
        .mapValues { it.value.map { it.second } }
    val updates = input.subList(emptyLineIndex + 1, input.size)
        .map {
            it.split(",").map { it.toInt() }
        }

    return updates.filter { update -> isValid(update, rules) }
        .sumOf { it[it.size / 2] }
        .toString()
}

private fun isValid(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
    return update.mapIndexed { index, number ->
        rules[number].orEmpty().all { ruleNumber ->
            val ruleNumberIndex = update.indexOf(ruleNumber).takeUnless { it == -1 } ?: Int.MAX_VALUE
            ruleNumberIndex > index
        }
    }.all { it }
}

fun part2(input: List<String>): String {
    val emptyLineIndex = input.indexOfFirst { it.isBlank() }
    val rules = input.subList(0, emptyLineIndex)
        .map {
            it.split("|").let {
                it[0].toInt() to it[1].toInt()
            }
        }.groupBy { it.first }
        .mapValues { it.value.map { it.second } }
    val updates = input.subList(emptyLineIndex + 1, input.size)
        .map {
            it.split(",").map { it.toInt() }
        }

    val notSortedUpdates = updates.filterNot { isValid(it, rules) }
    return notSortedUpdates.map {
        sortUpdate(it, rules)
    }.sumOf { it[it.size / 2] }
        .toString()
}

private fun sortUpdate(update: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
    val changedUpdate = update.toMutableList()

    while (!isValid(changedUpdate, rules)) {
        rules.forEach { rule ->
            val ruleFirst = rule.key
            rule.value.forEach { ruleSecond ->
                val firstIndex = changedUpdate.indexOf(ruleFirst).takeUnless { it == -1 }
                val secondIndex = changedUpdate.indexOf(ruleSecond).takeUnless { it == -1 }

                if (firstIndex != null && secondIndex != null && firstIndex > secondIndex) {
                    changedUpdate[firstIndex] = changedUpdate[secondIndex].also {
                        changedUpdate[secondIndex] = changedUpdate[firstIndex]
                    }
                }
            }
        }
    }

    return changedUpdate
}