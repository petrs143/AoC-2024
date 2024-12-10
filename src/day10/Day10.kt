package day10

import java.io.File

fun main() {
    val input = File("src/day10/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

private var visited: MutableList<MutableList<Int>> = emptyList<MutableList<Int>>().toMutableList()

private fun getScore(currentHeight: Int, row: Int, column: Int, input: List<List<Int>>, useVisited: Boolean): Int {
    if (row !in input.indices || column !in input[row].indices) {
        return 0
    }

    if (currentHeight != input[row][column]) {
        return 0
    }

    if (useVisited && visited[row][column] != -1) {
        return 0
    }

    visited[row][column] = currentHeight

    if (currentHeight == 9) {
        return 1
    }

    return getScore(currentHeight + 1, row + 1, column, input, useVisited) +
            getScore(currentHeight + 1, row - 1, column, input, useVisited) +
            getScore(currentHeight + 1, row, column + 1, input, useVisited) +
            getScore(currentHeight + 1, row, column - 1, input, useVisited)
}

fun part1(input: List<String>): String {
    val map = input.map {
        it.map { "$it".toInt() }
    }

    return map.mapIndexed { row, line ->
        line.mapIndexed { column, height ->
            if (height == 0) {
                visited = MutableList(map.size) { MutableList(map[0].size) { -1 } }
                getScore(0, row, column, map, useVisited = true)
            } else {
                0
            }
        }.sum()
    }.sum()
        .toString()
}

fun part2(input: List<String>): String {
    val map = input.map {
        it.map { "$it".toInt() }
    }

    return map.mapIndexed { row, line ->
        line.mapIndexed { column, height ->
            if (height == 0) {
                visited = MutableList(map.size) { MutableList(map[0].size) { -1 } }
                getScore(0, row, column, map, useVisited = false)
            } else {
                0
            }
        }.sum()
    }.sum()
        .toString()}