package day08

import println
import readInput

fun main() {
    fun parse(input: List<String>): Map<Char, List<Pair<Int, Int>>> {
        val frequencyLocations = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
        input.forEachIndexed { row, line ->
            line.forEachIndexed { column, c ->
                if (c != '.') {
                    frequencyLocations.getOrPut(c, { mutableListOf() })
                        .add(column to row)
                }
            }
        }
        return frequencyLocations
    }

    fun part1(input: List<String>): Int {
        operator fun Pair<Double, Double>.times(n: Int): Pair<Double, Double> {
            return (first * n) to (second * n)
        }

        operator fun Pair<Double, Double>.minus(vector: Pair<Double, Double>): Pair<Int, Int> {
            return (first - vector.first).toInt() to (second - vector.second).toInt()
        }

        operator fun Pair<Double, Double>.plus(vector: Pair<Double, Double>): Pair<Int, Int> {
            return (first + vector.first).toInt() to (second + vector.second).toInt()
        }

        val frequencyLocations = parse(input)

        return frequencyLocations.values.flatMap { locations ->
            val antinodes = mutableSetOf<Pair<Int, Int>>()
            val pairs = locations.flatMap { a -> locations.filter { b -> a != b }.map { b -> a to b } }.toSet()
            pairs.forEach { (a, b) ->
                val midpoint = (a.first + (b.first - a.first) / 2.0) to (a.second + (b.second - a.second) / 2.0)
                val antinodeVector = (b.first - a.first) * 1.5 to (b.second - a.second) * 1.5
                antinodes.add(midpoint + antinodeVector)
                antinodes.add(midpoint - antinodeVector)
            }

            antinodes
        }
            .toSet()
            .filter { (x, y) -> x in input[0].indices && y in input.indices }
            .size
    }

    fun part2(input: List<String>): Int {
        operator fun Pair<Int, Int>.times(n: Int): Pair<Int, Int> {
            return (first * n) to (second * n)
        }

        operator fun Pair<Int, Int>.minus(vector: Pair<Int, Int>): Pair<Int, Int> {
            return (first - vector.first) to (second - vector.second)
        }

        operator fun Pair<Int, Int>.plus(vector: Pair<Int, Int>): Pair<Int, Int> {
            return (first + vector.first) to (second + vector.second)
        }

        val frequencyLocations = parse(input)

        val antinodes = frequencyLocations.values.flatMap { locations ->
            val frequencyAntinodes = mutableSetOf<Pair<Int, Int>>()
            val pairs = locations.flatMap { a -> locations.filter { b -> a != b }.map { b -> a to b } }.toSet()

            pairs.forEach { (a, b) ->
                val antinodeVector = (b.first - a.first) to (b.second - a.second)

                var n = 0
                var antinode = a + antinodeVector * n
                while (antinode.let { (x, y) -> x in input[0].indices && y in input.indices }) {
                    frequencyAntinodes.add(antinode)
                    n += 1
                    antinode = a + antinodeVector * n
                }

                n = 0
                antinode = b - antinodeVector * n
                while (antinode.let { (x, y) -> x in input[0].indices && y in input.indices }) {
                    frequencyAntinodes.add(antinode)
                    n += 1
                    antinode = b - antinodeVector * n
                }
            }

            frequencyAntinodes
        }.toMutableSet()

        antinodes.addAll(frequencyLocations.values.filter { it.size > 1 }.flatten())

        return antinodes.size
    }

    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}
