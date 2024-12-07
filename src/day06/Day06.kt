package day06

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.print.attribute.standard.OrientationRequested

fun main() {
    val input = File("src/day06/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

data class Map(
    val entries: List<List<MapEntry>>
) {

    val rows = entries.size
    val columns = entries.firstOrNull()?.size ?: 0

    val visited = entries.sumOf { it.count { it.visited.isNotEmpty() } }

    fun withVisited(position: Position, orientation: Orientation): Map {
        return copy(
            entries = entries.mapIndexed { row, mapEntries ->
                mapEntries.mapIndexed { column, mapEntry ->
                    if (position.row == row && position.column == column) {
                        mapEntry.copy(visited = mapEntry.visited + orientation)
                    } else {
                        mapEntry
                    }
                }
            }
        )
    }

    fun withObstruction(position: Position): Map {
        return copy(
            entries = entries.mapIndexed { row, mapEntries ->
                mapEntries.mapIndexed { column, mapEntry ->
                    if (position.row == row && position.column == column) {
                        mapEntry.copy(type = Type.Obstruction)
                    } else {
                        mapEntry
                    }
                }
            }
        )
    }

    fun isOffMap(position: Position): Boolean {
        return position.row !in 0..<rows || position.column !in 0..<columns
    }

    fun hasObstruction(position: Position): Boolean {
        return entries.getOrNull(position.row)
            ?.getOrNull(position.column)
            ?.type == Type.Obstruction
    }

    fun isVisited(position: Position, orientation: Orientation): Boolean {
        return orientation in entries[position.row][position.column].visited
    }
}

data class MapEntry(
    val position: Position,
    val type: Type,
    val visited: Set<Orientation>,
)

data class Position(
    val row: Int,
    val column: Int,
) {

    fun move(orientation: Orientation): Position {
        return when (orientation) {
            Orientation.North -> Position(row - 1, column)
            Orientation.South -> Position(row + 1, column)
            Orientation.West -> Position(row, column - 1)
            Orientation.East -> Position(row, column + 1)
        }
    }
}

data class VisitedEntry(
    val position: Position,
    val orientation: Orientation,
)

sealed interface Type {

    data object Obstruction : Type
    data object Path : Type
}

enum class Orientation {
    North, South, West, East;

    fun turn(): Orientation {
        return when (this) {
            North -> East
            South -> West
            West -> North
            East -> South
        }
    }
}

fun findPath(map: Map, guardPosition: Position, orientation: Orientation): Map {
    if (map.isOffMap(guardPosition)) {
        return map
    }

    var newPosition = guardPosition.move(orientation)
    var newOrientation = orientation
    while (map.hasObstruction(newPosition)) {
        newOrientation = newOrientation.turn()
        newPosition = guardPosition.move(newOrientation)
    }

    return findPath(map.withVisited(guardPosition, orientation), newPosition, newOrientation)
}

fun part1(input: List<String>): String {
    var guardPosition: Position = Position(0, 0)
    val orientation = Orientation.North

    val map = input.mapIndexed { row, line ->
        line.mapIndexed { column, char ->
            val type = if (char == '#') {
                Type.Obstruction
            } else {
                Type.Path
            }

            if (char == '^') {
                guardPosition = Position(row, column)
            }

            MapEntry(Position(row, column), type, visited = emptySet())
        }
    }.let(::Map)

    return findPath(map, guardPosition, orientation).visited.toString()
}

fun findLoop(map: Map, guardPosition: Position, orientation: Orientation): Boolean {
    if (map.isOffMap(guardPosition)) {
        return false
    }

    if (map.isVisited(guardPosition, orientation)) {
        return true
    }

    var newPosition = guardPosition.move(orientation)
    var newOrientation = orientation

    repeat(4) {
        if (map.hasObstruction(newPosition)) {
            newOrientation = newOrientation.turn()
            newPosition = guardPosition.move(newOrientation)
        }
    }

    if (map.hasObstruction(newPosition)) {
        return true
    }

    return findLoop(map.withVisited(guardPosition, orientation), newPosition, newOrientation)
}

fun part2(input: List<String>): String {
    var guardPosition: Position = Position(0, 0)
    val orientation = Orientation.North

    val map = input.mapIndexed { row, line ->
        line.mapIndexed { column, char ->
            val type = if (char == '#') {
                Type.Obstruction
            } else {
                Type.Path
            }

            if (char == '^') {
                guardPosition = Position(row, column)
            }

            MapEntry(Position(row, column), type, visited = emptySet())
        }
    }.let(::Map)

    val mapWithPath = findPath(map, guardPosition, orientation)

    return runBlocking(Dispatchers.Default) {
        mapWithPath.entries.mapIndexed { _, line ->
            async {
                line.count {
                    if (it.type is Type.Path && it.visited.isNotEmpty()) {
                        val newMap = map.withObstruction(it.position)
                        findLoop(newMap, guardPosition, orientation)
                    } else {
                        false
                    }
                }
            }
        }.awaitAll()
            .sum()
            .toString()
    }
}