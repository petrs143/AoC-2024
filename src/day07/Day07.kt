package day07

import java.io.File

fun main() {
    val input = File("src/day07/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

data class Test(
    val result: Long,
    val numbers: List<Long>,
) {

    fun canBeSolved(): Boolean {
        return canBeSolved(0, 0)
    }

    private fun canBeSolved(index: Int, result: Long): Boolean {
        if (index >= numbers.size) {
            return result == this.result
        }

        return canBeSolved(index + 1, result + numbers[index]) ||
                canBeSolved(index + 1, result * numbers[index])
    }

    fun canBeSolvedWithConcatening(): Boolean {
        return canBeSolvedWithConcatening(0, 0)
    }

    private fun canBeSolvedWithConcatening(index: Int, result: Long): Boolean {
        if (index >= numbers.size) {
            return result == this.result
        }

        val concatenedResult = (result.toString() + numbers[index].toString()).toLong()

        return canBeSolvedWithConcatening(index + 1, result + numbers[index]) ||
                canBeSolvedWithConcatening(index + 1, result * numbers[index]) ||
                canBeSolvedWithConcatening(index + 1, concatenedResult)
    }
}

fun part1(input: List<String>): String {
    val tests = input.map {
        val (result, numbers) = it.split(':').let {
            it[0].toLong() to it[1].trim().split(' ').map { it.toLong() }
        }

        Test(result, numbers)
    }
    return tests.filter { it.canBeSolved() }
        .sumOf { it.result }
        .toString()
}

fun part2(input: List<String>): String {
    val tests = input.map {
        val (result, numbers) = it.split(':').let {
            it[0].toLong() to it[1].trim().split(' ').map { it.toLong() }
        }

        Test(result, numbers)
    }

    return tests.filter { it.canBeSolvedWithConcatening() }
        .sumOf { it.result }
        .toString()
}

