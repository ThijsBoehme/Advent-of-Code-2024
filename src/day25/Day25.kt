package day25

import println
import readInput

private typealias Key = List<Int>
private typealias Lock = List<Int>

fun main() {
    fun List<String>.transpose(): List<List<Char>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }

    fun parse(input: String): Pair<List<Key>, List<Lock>> {
        val schematics = input.split("\n\n")
            .map(String::lines)

        val locks: List<Lock> = schematics
            .filter { it.first().all { c -> c == '#' } }
            .map { schematic ->
                schematic.transpose()
                    .map { line -> line.count { it == '#' } - 1 }
            }

        val keys: List<Key> = schematics
            .filter { it.last().all { c -> c == '#' } }
            .map { schematic ->
                schematic.reversed()
                    .transpose()
                    .map { line -> line.count { it == '#' } - 1 }
            }

        return keys to locks
    }

    fun part1(input: Pair<List<Key>, List<Lock>>): Int {
        val (keys, locks) = input

        val combinations = buildSet {
            keys.forEach { key ->
                locks.forEach { lock ->
                    add(key to lock)
                }
            }
        }

        return combinations.count { (key, lock) ->
            key.zip(lock)
                .all { (keyRow, lockRow) -> keyRow + lockRow <= 5 }
        }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    val testInput = readInput("day25/Day25_test").joinToString("\n")
    check(part1(parse(testInput)) == 3)
    // check(part2(parse(testInput)) == )

    val input = readInput("day25/Day25").joinToString("\n")
    part1(parse(input)).println()
    // part2(input).println()
}
