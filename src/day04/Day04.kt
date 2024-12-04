package day04

import java.io.File

fun main() {
    val input = File("src/day04/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

fun part1(input: List<String>): String {
    return input.mapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, _ ->
            Direction.entries.sumOf { direction ->
                searchXmas(input, rowIndex, columnIndex, direction, "XMAS")
            }
        }.sum()
    }.sum()
        .toString()
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT
}

fun searchXmas(input: List<String>, row: Int, column: Int, direction: Direction, searching: String): Int {
    if (row !in input.indices || column !in input[row].indices) {
        return 0
    }

    if (searching.length == 1 && input[row][column] == searching.first()) {
        return 1
    }

    if (input[row][column] != searching.first()) {
        return 0
    }

    val newSearching = searching.substring(1)
    return when (direction) {
        Direction.UP -> searchXmas(input, row - 1, column, direction, newSearching)
        Direction.DOWN -> searchXmas(input, row + 1, column, direction, newSearching)
        Direction.LEFT -> searchXmas(input, row, column - 1, direction, newSearching)
        Direction.RIGHT -> searchXmas(input, row, column + 1, direction, newSearching)
        Direction.UP_RIGHT -> searchXmas(input, row - 1, column + 1, direction, newSearching)
        Direction.UP_LEFT -> searchXmas(input, row - 1, column - 1, direction, newSearching)
        Direction.DOWN_RIGHT -> searchXmas(input, row + 1, column + 1, direction, newSearching)
        Direction.DOWN_LEFT -> searchXmas(input, row + 1, column - 1, direction, newSearching)
    }
}

fun part2(input: List<String>): String {
    return input.mapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, char ->
            if (char == 'A') {
                isValid(input, rowIndex, columnIndex)
            } else {
                false
            }
        }.count { it }
    }.sum()
        .toString()
}

private fun isValid(input: List<String>, row: Int, column: Int): Boolean {
    val first = buildSet {
        input.getOrNull(row - 1)?.getOrNull(column - 1)?.let {
            add(it)
        }

        add(input[row][column])

        input.getOrNull(row + 1)?.getOrNull(column + 1)?.let {
            add(it)
        }
    }.sorted()

    val second = buildSet {
        input.getOrNull(row - 1)?.getOrNull(column + 1)?.let {
            add(it)
        }

        add(input[row][column])

        input.getOrNull(row + 1)?.getOrNull(column - 1)?.let {
            add(it)
        }
    }.sorted()

    return first == second && first == setOf('A', 'M', 'S').sorted()
}