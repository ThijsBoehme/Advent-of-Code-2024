package day09

import println
import readInput

fun main() {
    fun part1(input: String): Long {
        val disk = ArrayDeque<Int>()

        input.forEachIndexed { index, c ->
            if (index % 2 == 0) {
                repeat(c.digitToInt()) { disk.add(index / 2) }
            } else {
                repeat(c.digitToInt()) { disk.add(-1) }
            }
        }

        var position = 0
        var sum = 0L

        while (disk.isNotEmpty()) {
            var fileId = disk.removeFirstOrNull()
            while (fileId == -1) {
                fileId = disk.removeLastOrNull()
            }
            sum += position * (fileId ?: break)
            position++
        }

        return sum
    }

    fun part2(input: String): Long {
        val disk = ArrayDeque<Pair<Int, Int>>()

        input.forEachIndexed { index, c ->
            if (index % 2 == 0) {
                disk.add(index / 2 to c.digitToInt())
            } else {
                disk.add(-1 to c.digitToInt())
            }
        }

        val defragmentedDisk = ArrayDeque<Pair<Int, Int>>()

        while (disk.isNotEmpty()) {
            // Remove blocks with length 0
            if (disk.first().second == 0) disk.removeFirst()

            when (disk.first().first) {
                // If we are at an empty space
                -1 -> {
                    var freeSpaces = disk.first().second
                    // Search from the back for block that can fit the empty space
                    var replacement = disk.findLast { (id, amount) -> id != -1 && amount <= freeSpaces }

                    while (freeSpaces > 0 && replacement != null) {
                        // If there's a block, reduce the empty space with the size of the block and add it to the defragmented disk
                        freeSpaces -= replacement.second
                        defragmentedDisk.add(replacement)

                        // Replace block that we use with empty spaces
                        val index = disk.indexOf(replacement)
                        disk.remove(replacement)
                        disk.add(index, -1 to replacement.second)

                        // Remove empty spaces at front of the disk
                        disk.removeFirst()
                        if (freeSpaces > 0) disk.addFirst(-1 to freeSpaces)

                        // If there's free space left over, search for another file block that fits
                        replacement = disk.findLast { (id, amount) -> id != -1 && amount <= freeSpaces }
                    }

                    // If there's no file blocks that fit the empty space, add the empty space to the defragmented disk
                    if (freeSpaces > 0) {
                        defragmentedDisk.add(disk.removeFirst())
                    }
                }

                // If the next block of the disk is a file, just add it to the defragmented disk
                else -> defragmentedDisk.add(disk.removeFirst())
            }
        }

        var position = 0
        var sum = 0L

        while (defragmentedDisk.isNotEmpty()) {
            val block = defragmentedDisk.removeFirst()
            repeat(block.second) {
                if (block.first != -1) sum += position * block.first
                position++
            }
        }

        return sum
    }

    val testInput = readInput("day09/Day09_test").first()
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("day09/Day09").first()
    part1(input).println()
    part2(input).println()
}
