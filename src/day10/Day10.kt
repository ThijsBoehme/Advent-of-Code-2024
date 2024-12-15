package day10

import println
import readInput

fun main() {
    fun parse(input: List<String>): List<List<Int>> {
        val map = input.map {
            it.toCharArray().map { c ->
                c.digitToIntOrNull() ?: -1
            }
        }
        return map
    }

    fun countPaths(map: List<List<Int>>, x: Int, y: Int): List<Pair<Int, Int>> {
        val paths = mutableListOf<Pair<Int, Int>>()
        val currentHeight = map[y][x]
        if (currentHeight == 9) return listOf(x to y)
        if (map.getOrNull(y - 1)?.get(x) == currentHeight + 1) paths += countPaths(map, x, y - 1)
        if (map.getOrNull(y + 1)?.get(x) == currentHeight + 1) paths += countPaths(map, x, y + 1)
        if (map[y].getOrNull(x - 1) == currentHeight + 1) paths += countPaths(map, x - 1, y)
        if (map[y].getOrNull(x + 1) == currentHeight + 1) paths += countPaths(map, x + 1, y)
        return paths
    }

    fun startPositions(map: List<List<Int>>): MutableList<Pair<Int, Int>> {
        val startPositions = mutableListOf<Pair<Int, Int>>()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, height ->
                if (height == 0) startPositions.add(x to y)
            }
        }
        return startPositions
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)

        val startPositions = startPositions(map)

        return startPositions.sumOf { countPaths(map, it.first, it.second).toSet().count() }
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)

        val startPositions = startPositions(map)

        return startPositions.sumOf { countPaths(map, it.first, it.second).size }
    }

    check(
        part1(
            """
                0123
                1234
                8765
                9876
                """.trimIndent().lines()
        ) == 1
    )
    check(
        part1(
            """
                ...0...
                ...1...
                ...2...
                6543456
                7.....7
                8.....8
                9.....9
                """.trimIndent().lines()
        ) == 2
    )
    check(
        part1(
            """
                ..90..9
                ...1.98
                ...2..7
                6543456
                765.987
                876....
                987....
                """.trimIndent().lines()
        ) == 4
    )
    check(
        part1(
            """
                10..9..
                2...8..
                3...7..
                4567654
                ...8..3
                ...9..2
                .....01
                """.trimIndent().lines()
        ) == 3
    )
    check(
        part1(
            """
                89010123
                78121874
                87430965
                96549874
                45678903
                32019012
                01329801
                10456732""".trimIndent().lines()
        ) == 36
    )

    check(
        part2(
            """
                .....0.
                ..4321.
                ..5..2.
                ..6543.
                ..7..4.
                ..8765.
                ..9....""".trimIndent().lines()
        ) == 3
    )
    check(
        part2(
            """
                ..90..9
                ...1.98
                ...2..7
                6543456
                765.987
                876....
                987....""".trimIndent().lines()
        ) == 13
    )
    check(
        part2(
            """
                012345
                123456
                234567
                345678
                4.6789
                56789.""".trimIndent().lines()
        ) == 227
    )
    check(
        part2(
            """
                89010123
                78121874
                87430965
                96549874
                45678903
                32019012
                01329801
                10456732""".trimIndent().lines()
        ) == 81
    )

    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}
