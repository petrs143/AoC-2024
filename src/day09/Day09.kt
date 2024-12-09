package day09

import java.io.File

fun main() {
    val input = File("src/day09/input.txt").readLines()
    println(part1(input))
    println(part2(input))
};

data class Disk(
    val blocks: List<Block>
) {

    val checksum = blocks.mapIndexed { index, block ->
        when (block) {
            is Block.File -> block.id.toLong() * index
            Block.Space -> 0L
        }
    }.sum()

    fun compact(): Disk {
        var i = 0
        var j = blocks.lastIndex
        val newBlocks = blocks.toMutableList()

        while (i <= j) {
            if (newBlocks[i] is Block.File) {
                i++
                continue
            }

            if (newBlocks[j] is Block.Space) {
                j--
                continue
            }

            newBlocks[i] = newBlocks[j].also {
                newBlocks[j] = newBlocks[i]
            }
        }

        return Disk(newBlocks)
    }

    fun compatPart2(): Disk {
        var j = blocks.lastIndex
        var jSize = 0
        val newBlocks = blocks.toMutableList()

        while (j >= 0) {
            if (newBlocks[j] is Block.Space) {
                j--
                jSize = 0
                continue
            }

            while (newBlocks.getOrNull(j - jSize) == newBlocks[j]) {
                jSize++
            }

            var i = 0
            var iSize = 0
            while (i <= j) {
                if (newBlocks[i] is Block.File) {
                    i++
                    iSize = 0
                    continue
                }

                while (newBlocks.getOrNull(i + iSize) == newBlocks[i]) {
                    iSize++
                }

                if (iSize >= jSize) {
                    repeat(jSize) { index ->
                        newBlocks[i + index] = newBlocks[j - index].also {
                            newBlocks[j - index] = newBlocks[i + index]
                        }
                    }
                }

                i += iSize
                iSize = 0

            }

            j -= jSize
            jSize = 0
        }

        return Disk(newBlocks)
    }
}

sealed interface Block {

    data class File(val id: Int) : Block

    data object Space : Block
}

fun part1(input: List<String>): String {
    val line = input.first()

    var fileId = 0
    val disk = line.flatMapIndexed { index, char ->
        val size = char.toString().toInt()
        if (index % 2 == 0) {
            List(size) { Block.File(fileId) }.also {
                fileId++
            }
        } else {
            List(size) { Block.Space }
        }
    }.let(::Disk)

    return disk.compact()
        .checksum
        .toString()
}

fun part2(input: List<String>): String {
    val line = input.first()

    var fileId = 0
    val disk = line.flatMapIndexed { index, char ->
        val size = char.toString().toInt()
        if (index % 2 == 0) {
            List(size) { Block.File(fileId) }.also {
                fileId++
            }
        } else {
            List(size) { Block.Space }
        }
    }.let(::Disk)

    return disk.compatPart2()
        .checksum
        .toString()
}

