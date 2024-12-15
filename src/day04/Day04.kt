package day04

import println
import readInput

fun main() {
    fun part1(rows: List<String>): Int {
        val directions = listOf(
            1 to 0,
            1 to 1,
            0 to 1,
            -1 to 1,
            -1 to 0,
            -1 to -1,
            0 to -1,
            1 to -1,
        )

        return rows.mapIndexed { y, row ->
            row.mapIndexed { x, letter ->
                if (letter == 'X') {
                    directions.count { (dx, dy) ->
                        val m = rows.getOrNull(y + dy)?.getOrNull(x + dx)
                        val a = rows.getOrNull(y + 2 * dy)?.getOrNull(x + 2 * dx)
                        val s = rows.getOrNull(y + 3 * dy)?.getOrNull(x + 3 * dx)
                        m?.equals('M') ?: false &&
                                a?.equals('A') ?: false &&
                                s?.equals('S') ?: false
                    }
                } else {
                    0
                }
            }.sum()
        }.sum()
    }

    fun part2(rows: List<String>): Int {
        val directions = listOf(
            1 to 1,
            -1 to 1,
            -1 to -1,
            1 to -1,
        ) // Only diagonals

        return rows.mapIndexed { y, row ->
            row.filterIndexed { x, letter ->
                if (letter == 'A') {
                    directions.count { (dx, dy) ->
                        val m = rows.getOrNull(y - dy)?.getOrNull(x - dx)
                        val s = rows.getOrNull(y + dy)?.getOrNull(x + dx)
                        m?.equals('M') ?: false &&
                                s?.equals('S') ?: false
                    } == 2 // Only count 'A's that occur in two 'MAS'ses
                } else {
                    false
                }
            }.count()
        }.sum()
    }

    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}
