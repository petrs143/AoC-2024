package day08

import java.io.File

fun main() {
    val input = File("src/day08/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

data class Map(
    val points: List<List<MapPoint>>
) {

    val antinodesCount = points.sumOf { it.count { it.hasAntinode } }

    fun addAntinodes(): Map {
        val antennas = points.flatMap { it.filterIsInstance<MapPoint.Antenna>() }
        val groups = antennas.groupBy { it.freq }
        val antinodes = groups.mapValues { (_, groupAntennas) ->
            getAntinodePositions(groupAntennas)
        }.values.flatten()

        val newPoints = points.mapIndexed { row, line ->
            line.mapIndexed { column, mapPoint ->
                if ((row to column) in antinodes) {
                    mapPoint.withAntinode()
                } else {
                    mapPoint
                }
            }
        }

        return copy(points = newPoints)
    }

    private fun getAntinodePositions(antennas: List<MapPoint.Antenna>): List<Pair<Int, Int>> {
        return antennas.flatMapIndexed { i, iAntenna ->
            antennas.mapIndexedNotNull { j, jAntenna ->
                if (iAntenna != jAntenna) {
                    val rowDistance = iAntenna.row - jAntenna.row
                    val columnDistance = iAntenna.column - jAntenna.column

                    iAntenna.row + rowDistance to iAntenna.column + columnDistance
                } else {
                    null
                }
            }
        }
    }

    fun addAntinodesPart2(): Map {
        val antennas = points.flatMap { it.filterIsInstance<MapPoint.Antenna>() }
        val groups = antennas.groupBy { it.freq }
        val antinodes = groups.mapValues { (_, groupAntennas) ->
            getAntinodePositionsPart2(groupAntennas)
        }.values.flatten()

        val newPoints = points.mapIndexed { row, line ->
            line.mapIndexed { column, mapPoint ->
                if ((row to column) in antinodes) {
                    mapPoint.withAntinode()
                } else {
                    mapPoint
                }
            }
        }

        return copy(points = newPoints)
    }

    private fun getAntinodePositionsPart2(antennas: List<MapPoint.Antenna>): List<Pair<Int, Int>> {
        return antennas.flatMapIndexed { i, iAntenna ->
            antennas.mapIndexedNotNull { j, jAntenna ->
                if (iAntenna != jAntenna) {
                    val rowDistance = iAntenna.row - jAntenna.row
                    val columnDistance = iAntenna.column - jAntenna.column
                    var antinodeIndex = 0
                    val positions = mutableListOf<Pair<Int, Int>>()
                    while (true) {
                        val antinodeRow = iAntenna.row + antinodeIndex * rowDistance
                        val antinodeColumn = iAntenna.column + antinodeIndex * columnDistance

                        if (antinodeRow !in points.indices || antinodeColumn !in points[antinodeRow].indices) {
                            break
                        }

                        positions.add(antinodeRow to antinodeColumn)
                        antinodeIndex++
                    }

                    positions
                } else {
                    null
                }
            }.flatten()
        }
    }
}

sealed interface MapPoint {

    val hasAntinode: Boolean
    val row: Int
    val column: Int

    fun withAntinode(): MapPoint

    data class Antenna(
        override val hasAntinode: Boolean,
        override val row: Int,
        override val column: Int,
        val freq: Char
    ) : MapPoint {

        override fun withAntinode(): MapPoint {
            return copy(hasAntinode = true)
        }
    }

    data class Empty(
        override val hasAntinode: Boolean,
        override val row: Int,
        override val column: Int,
    ) : MapPoint {

        override fun withAntinode(): MapPoint {
            return copy(hasAntinode = true)
        }
    }

}


fun part1(input: List<String>): String {
    val map = input.mapIndexed { row, line ->
        line.mapIndexed { column, char ->
            if (char == '.') {
                MapPoint.Empty(hasAntinode = false, row = row, column = column)
            } else {
                MapPoint.Antenna(freq = char, hasAntinode = false, row = row, column = column)
            }
        }
    }.let(::Map)

    return map.addAntinodes()
        .antinodesCount
        .toString()
}

fun part2(input: List<String>): String {
    val map = input.mapIndexed { row, line ->
        line.mapIndexed { column, char ->
            if (char == '.') {
                MapPoint.Empty(hasAntinode = false, row = row, column = column)
            } else {
                MapPoint.Antenna(freq = char, hasAntinode = false, row = row, column = column)
            }
        }
    }.let(::Map)

    return map.addAntinodesPart2()
        .antinodesCount
        .toString()
}